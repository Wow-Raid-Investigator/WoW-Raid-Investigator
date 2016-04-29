using System;
using Perst;

// Enums are supported by Perst
enum MyEnum 
{
   First,
   Second,
   Third
};

// All presistent capable classes should be derived from Persistent base class
class MyPersistentClass  : Persistent 
{
    private int      intKey; // integer key
    private string   strKey; // string key
    private string   body;   // non-indexed field
    private DateTime timestamp; // DateTime type is built-in type for Perst
    private MyEnum   e;

    public MyPersistentClass(int intKey, string strKey, string body, MyEnum e) 
    {
        this.intKey = intKey;
        this.strKey = strKey;
        this.body = body;
        this.timestamp = DateTime.Now;
        this.e = e;
    }
    
    public int IntKey
    {
        get
        {
            return intKey;
        }
        set
        {
            intKey = value;
        }
    }    

    public string StrKey
    {
        get
        {
            return strKey;
        }
    }    

    public string Body
    {
        get
        {
            return body;
        }
    }    

    public DateTime Timestamp
    {
        get
        {
            return timestamp;
        }
    }    


    public override String ToString() { 
        return "{intKey=" + intKey + ", strKey=" + strKey + ", body=" + body + ", e=" + e + ", timesamp=" + timestamp + "}";
    }
}

// There should one root object in the database. This class should contain
// collections which can be used to access all other objects in the storage.
class MyRootClass : Persistent 
{
#if USE_GENERICS
    public FieldIndex<int,MyPersistentClass>    intKeyIndex;  // index on MyPersistentClass.intKey
    public FieldIndex<string,MyPersistentClass> strKeyIndex;  // index on MyPersistentClass.strKey
    public Index<int,MyPersistentClass>         foreignIndex; // index on MyPersistentClass which key is not part of this class 
#else
    public FieldIndex intKeyIndex;  // index on MyPersistentClass.intKey
    public FieldIndex strKeyIndex;  // index on MyPersistentClass.strKey
    public Index      foreignIndex; // index on MyPersistentClass which key is not part of this class 
#endif

    // Persistent capable class should not use default constructor for initialization, since
    // this constructor is called by Perst each time the object is loaded from the database.
    // So persistent capable class should have not constructor at all (in this case 
    // it will be automatically generated by compiler), either define constructor with non-empty
    // parameter list for object initialization (the most natural way is to pass Storage reference 
    // to this constructor, since Storage in any case is neede to created instances of Perst 
    // collections) and define empty default constructor (in case of using Sun JVM, definition of 
    // empty constructor is not necessary since Perst is able to instantiate persistent object
    // without using default constructor using non-standard Sun's API).
    public MyRootClass(Storage db) : base(db)
    {         
#if USE_GENERICS
        intKeyIndex = db.CreateFieldIndex<int, MyPersistentClass>(
            "intKey", // name of indexed field
            true); // unique index
        strKeyIndex = db.CreateFieldIndex<string, MyPersistentClass>(
            "StrKey",  // name of indexed property
            false); // index allows duplicates (is not unique)
        foreignIndex = db.CreateIndex<int, MyPersistentClass>(
            false); // index allows duplicates (is not unique)
#else 
        intKeyIndex = db.CreateFieldIndex(
            typeof(MyPersistentClass), // class for which index is defined
            "intKey", // name of indexed field
            true); // unique index
        strKeyIndex = db.CreateFieldIndex(
            typeof(MyPersistentClass), // class for which index is defined
            "StrKey",  // name of indexed property
            false); // index allows duplicates (is not unique)
        foreignIndex = db.CreateIndex(
            typeof(int), // key type
            false); // index allows duplicates (is not unique)
#endif      
    }

    // Default constructor is needed for Perst to be able tio instantiate instances of loaded
    // objects. In case of using Sun JVM, it can be skept
    public MyRootClass() {} 
}


public class Simple 
{
    const int PagePoolSize = 32*1024*1024; // database cache size

    static public void Main(string[] args) 
    {    
        // get instance of the storage
        Storage db = StorageFactory.Instance.CreateStorage();

        // open the database 
        db.Open("simple.dbs", PagePoolSize);

        MyRootClass root = (MyRootClass)db.Root; // get storage root
        if (root == null) 
        { 
            // Root is not yet defined: stotage is not initialized
            root = new MyRootClass(db); // create root object
            db.Root = root; // register root object
        }
        // Create instance of the persistent capable class.
        // Created instance is not automatically stored in the database: Perst uses 
        // "Persistence by reachability" apporach, which means that persistent capable class with
        // be automatically stored in the database when reference to it is assigned to some other 
        // persistent object (including collections).
        MyPersistentClass obj = new MyPersistentClass(1, "A.B", "Hello world", MyEnum.First);
        
        // It is responsibility of programmer in Perst to maintain indices: add crearted object
        // to the proper indices, exclude it from the indices when key fields are changed or object 
        // is deleted.
        root.intKeyIndex.Put(obj); // add object to index on intKey field
        root.strKeyIndex.Put(obj); // add object to index in strKey field

        // To explictiely specify value of the key it is necessary to create instance of 
        // org.garret.perst.Key class which overloaded constructor will create key of correspondent type
        root.foreignIndex.Put(new Key(1001), obj);

        // Commit current transaction. It should not be done after insertion of each object since 
        // transaction commit is expensibe operation and too frequent commits leans to bad performance.
        // It is preferrable to group sequence of logicaslly relation operations into one transaction
        db.Commit();

        // Locate object by key. Since index is unique it is possible to use get method which returns
        // single object or null if object with such key is not found
        int intKeyVal = 1;
#if USE_GENERICS
        obj = root.intKeyIndex[intKeyVal];
#else
        obj = (MyPersistentClass)root.intKeyIndex[intKeyVal];
#endif
        Console.WriteLine("Exact match search by intKey: " + obj);

        // Since strKeyIndex is not unique, it is necessary to user GenericIndex.get method which returns 
        // array of objects. It takes minimal and maximal value of the key as parameters.
        // In case of strict match search, the same key should be specified as minimam and maximim
        string strKeyValue = "A.B";
#if USE_GENERICS   
        MyPersistentClass[] result = root.strKeyIndex[strKeyValue, strKeyValue];
#else
        object[] result = root.strKeyIndex[strKeyValue, strKeyValue];
#endif
        for (int i = 0; i < result.Length; i++) { 
            Console.WriteLine("Exact match search by strKey: " + result[i]);
        }
        
        // Get iterator through records belonging to specified key range in ascent order
        foreach (MyPersistentClass o in root.foreignIndex.Range(
            new Key(100, true), // inclusive low boundary
            new Key(10000, false), // exclusive high boundary
            IterationOrder.AscentOrder)) // ascent order 
        {
            Console.WriteLine("Range search by foreign key: " + o);
        }

        // Locate all objects which strKey starts with prefix "A."
        foreach (MyPersistentClass o in root.strKeyIndex.StartsWith("A.")) 
        { 
            Console.WriteLine("Search by prefix: " + o);
        }
        
        // Locate all objects which strKey is prefix of "A.B.C"
        result = root.strKeyIndex.PrefixSearch("A.B.C");
        for (int i = 0; i < result.Length; i++) 
        { 
            Console.WriteLine("Locate prefix: " + result[i]);
        }
        
        // To update object it is necessary first to exclude it from the index:
        root.intKeyIndex.Remove(obj);
        // ... then update the field
        obj.IntKey = 2;
        // ... and insert it in the index once again
        root.intKeyIndex.Put(obj);


        // When object is removed, it should be first excluded from all indices
        root.intKeyIndex.Remove(obj); // when object is removed from field index, it is not neccesary explicitely specify key
        root.strKeyIndex.Remove(obj);
        root.foreignIndex.Remove(1001, obj); // ... and here key has to be explicitely specified
        obj.Deallocate(); // explicit deallocation of object (Perst garbage collection can be used instead of explicit deallocation

        // Close the database
        db.Close();
    }
}
        
        