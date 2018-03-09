#!/usr/bin/env python
# encoding: utf-8
"""
@module : MySQLDAO
@author : Rinkako
@time   : 2017/12/27
"""

import mysql.connector
import threading
import GlobalConfigContext as GCC
from DAO.AbstractDAO import AbstractDAO
from Utility.LogUtil import LogUtil


class MySQLDAO(AbstractDAO):
    """
    This Data Access Object class provides functions for MySQL operations.
    """

    def __init__(self):
        """
        Create a new DAO.
        """
        super(MySQLDAO, self).__init__()
        self._executeMutex = threading.RLock()

    def _initialize(self):
        """
        Initialize the connection to MySQL server
        """
        self.Config = {'host': GCC.RDBHOST,
                       'user': GCC.RDBUSER,
                       'password': GCC.RDBPASSWORD,
                       'port': GCC.RDBPORT,
                       'database': GCC.RDBPATH,
                       'charset': 'utf8'}
        try:
            self.Connection = mysql.connector.connect(**self.Config)
            self.Connection.autocommit = True
        except mysql.connector.Error as e:
            LogUtil.ErrorLog("In _initialize, " + e.message, MySQLDAO.__name__, "")
            raise

    def _dispose(self):
        """
        Dispose the connection to MySQL server
        """
        try:
            self.Connection.close()
        except mysql.connector.Error as e:
            LogUtil.ErrorLog("In _dispose, " + e.message, MySQLDAO.__name__, "")

    def ShowTables(self):
        """
        Get all tables name.
        :return: a list of table name
        """
        return self.ExecuteSQL('show tables', needRet=True)

    def DescribeTable(self, tableName):
        """
        Get descriptor of a table.
        :return: descriptor list
        """
        return self.ExecuteSQL('describe %s' % tableName, needRet=True)

    def Cat(self, tableName, limit):
        """
        Get the front rows of a table.
        :param tableName: table name
        :param limit: row count
        :return: a list of row data
        """
        return self.ExecuteSQL('SELECT * FROM %s LIMIT %s' % (tableName, limit), needRet=True)

    def ExecuteSQL(self, sql, needRet=True, dp=0, updateVersion=True):
        """
        Execute a specific SQL and update data version if need.
        :param sql: sql string
        :param needRet: need return execution result
        :param dp: depth of exception stack
        :param updateVersion: need update data version
        :return: execution result table
        """
        retVal = self.ActualExecuteSQL(sql, needRet=needRet, dp=dp)
        try:
            if updateVersion is True and sql.startswith('SELECT') is False:
                import uuid
                vsql = "UPDATE ren_cconfig SET rvalue = 'COrganDataVer_%s' WHERE rkey = 'dataVersion'" % uuid.uuid1()
                self.ActualExecuteSQL(vsql, needRet=needRet, dp=dp)
        except:
            LogUtil.ErrorLog('COrgan cannot update data version', 'MySQLDAO')
        return retVal

    def ActualExecuteSQL(self, sql, needRet=True, dp=0):
        """
        Execute a specific SQL.
        :param sql: sql string
        :param needRet: need return execution result
        :param dp: depth of exception stack
        :return: execution result table
        """
        self._executeMutex.acquire()
        cursor = None
        try:
            cursor = self.Connection.cursor(buffered=True)
            cursor.execute(sql)
            if needRet is True:
                if cursor.description is not None:
                    values = cursor.fetchall()
                    names = [cd[0] for cd in cursor.description]
                    return [dict(zip(names, v)) for v in values]
                else:
                    return cursor.fetchall()
            else:
                return None
        except mysql.connector.Error as e:
            try:
                if dp > 1:
                    return
                LogUtil.ErrorLog("In ExecuteSQL, " + str(e) + " | Query: << " + (u"%s" % sql) + " >>",
                                 MySQLDAO.__name__, dp=dp+1)
            except:
                from traceback import format_exc
                print 'ExecuteSQL Exception:'
                print format_exc()
        finally:
            if cursor is not None:
                cursor.close()
            self._executeMutex.release()
