package com.hufudb.onedb.owner.adapter.sqlite;

import java.sql.Connection;
import java.sql.Statement;
import com.hufudb.onedb.expression.Translator;
import com.hufudb.onedb.owner.adapter.AdapterTypeConverter;
import com.hufudb.onedb.owner.adapter.jdbc.JDBCAdapter;

public class SqliteAdapter extends JDBCAdapter {
  SqliteAdapter(String catalog, Connection connection, Statement statement,
      AdapterTypeConverter converter, Translator translator) {
    super(catalog, connection, statement, converter, translator);
  }
}
