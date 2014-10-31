package com.cmk.utc.db.executor;


import java.sql.Connection;


public class ConnectionStatus {
	private Connection conn;
	private boolean isTrans = false;

    public ConnectionStatus(Connection conn, boolean isTrans) {
		this.conn = conn;
		this.isTrans = isTrans;
	}

	public Connection getConnection() {
		return conn;
	}

	public boolean isTrans() {
		return isTrans;
	}
}
