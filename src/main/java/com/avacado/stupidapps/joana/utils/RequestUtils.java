package com.avacado.stupidapps.joana.utils;

public class RequestUtils
{

  private static ThreadLocal<String> databaseName = new ThreadLocal<>();

  public static String getDatabaseName()
  {
    return databaseName.get();
  }

  public static void setDatabaseName(String databaseName)
  {
    RequestUtils.databaseName.set(databaseName);
  }
  
}
