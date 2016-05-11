package handlers;

import com.datastax.driver.core.BoundStatement;

public interface Container {
	public BoundStatement getStatement(BoundStatement input);
}
