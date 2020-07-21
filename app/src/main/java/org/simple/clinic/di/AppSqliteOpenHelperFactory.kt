package org.simple.clinic.di

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteTransactionListener
import android.os.CancellationSignal
import android.util.Pair
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteStatement
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteDatabaseConfiguration
import timber.log.Timber
import java.util.Locale

/**
 * Instance of [SupportSQLiteOpenHelper.Factory] that does the following:
 * - Creates an instance of [RequerySQLiteOpenHelperFactory] internally
 * - Has a flag to set whether it should be an in-memory database
 *
 * **Note:** There is a reason why the configuration for in-memory databases was moved to this class
 * instead of via Room's database builder. `sqlite-android`'s default in-memory implementation ignores
 * all dynamic extensions in the configuration and hence, testing the databases while using the extension
 * is a lot more problematic.
 *
 * However, by making it a normal database in Room, and overriding the configuration provided in this
 * class, we can make it an in-memory database with the extension loaded
 **/
class AppSqliteOpenHelperFactory(inMemory: Boolean = false) : SupportSQLiteOpenHelper.Factory {

  private val factory: SupportSQLiteOpenHelper.Factory

  init {
    factory = ProfilingSqliteOpenHelper.Factory(inMemory)
  }

  override fun create(configuration: SupportSQLiteOpenHelper.Configuration?) = factory.create(configuration)!!
}

private class ProfilingSqliteOpenHelper(
    private val wrapped: SupportSQLiteOpenHelper
) : SupportSQLiteOpenHelper {

  override fun getDatabaseName(): String {
    return wrapped.databaseName
  }

  override fun getWritableDatabase(): SupportSQLiteDatabase {
    return ProfilingSqliteDatabase(wrapped.writableDatabase)
  }

  override fun getReadableDatabase(): SupportSQLiteDatabase {
    return ProfilingSqliteDatabase(wrapped.readableDatabase)
  }

  override fun close() {
    wrapped.close()
  }

  override fun setWriteAheadLoggingEnabled(enabled: Boolean) {
    wrapped.setWriteAheadLoggingEnabled(enabled)
  }

  class Factory(inMemory: Boolean = false) : SupportSQLiteOpenHelper.Factory {

    private val factory: RequerySQLiteOpenHelperFactory

    init {
      factory = RequerySQLiteOpenHelperFactory(createConfigurations(inMemory))
    }

    private fun createConfigurations(inMemory: Boolean) =
        listOf(RequerySQLiteOpenHelperFactory.ConfigurationOptions { config ->
          if (inMemory) {
            SQLiteDatabaseConfiguration(SQLiteDatabaseConfiguration.MEMORY_DB_PATH, SQLiteDatabase.CREATE_IF_NECESSARY)
          } else {
            config
          }
        })

    override fun create(configuration: SupportSQLiteOpenHelper.Configuration?): SupportSQLiteOpenHelper {
      return ProfilingSqliteOpenHelper(factory.create(configuration))
    }
  }
}

private class ProfilingSqliteDatabase(
    private val wrapped: SupportSQLiteDatabase
) : SupportSQLiteDatabase {

  override fun setMaximumSize(numBytes: Long): Long {
    return wrapped.setMaximumSize(numBytes)
  }

  override fun insert(table: String?, conflictAlgorithm: Int, values: ContentValues?): Long {
    return profileWithValue { wrapped.insert(table, conflictAlgorithm, values) }
  }

  override fun enableWriteAheadLogging(): Boolean {
    return wrapped.enableWriteAheadLogging()
  }

  override fun isDatabaseIntegrityOk(): Boolean {
    return wrapped.isDatabaseIntegrityOk
  }

  override fun isWriteAheadLoggingEnabled(): Boolean {
    return wrapped.isWriteAheadLoggingEnabled
  }

  override fun disableWriteAheadLogging() {
    wrapped.disableWriteAheadLogging()
  }

  override fun compileStatement(sql: String?): SupportSQLiteStatement {
    return ProfilingSqliteStatement(wrapped.compileStatement(sql))
  }

  override fun beginTransactionWithListenerNonExclusive(transactionListener: SQLiteTransactionListener?) {
    wrapped.beginTransactionWithListenerNonExclusive(transactionListener)
  }

  override fun isDbLockedByCurrentThread(): Boolean {
    return wrapped.isDbLockedByCurrentThread
  }

  override fun setPageSize(numBytes: Long) {
    wrapped.pageSize = numBytes
  }

  override fun query(query: String?): Cursor {
    return profileWithValue { wrapped.query(query) }
  }

  override fun query(query: String?, bindArgs: Array<out Any>?): Cursor {
    return profileWithValue { wrapped.query(query, bindArgs) }
  }

  override fun query(query: SupportSQLiteQuery?): Cursor {
    return profileWithValue { wrapped.query(query) }
  }

  override fun query(query: SupportSQLiteQuery?, cancellationSignal: CancellationSignal?): Cursor {
    return profileWithValue { wrapped.query(query, cancellationSignal) }
  }

  override fun endTransaction() {
    wrapped.endTransaction()
  }

  override fun getMaximumSize(): Long {
    return wrapped.maximumSize
  }

  override fun setLocale(locale: Locale?) {
    wrapped.setLocale(locale)
  }

  override fun beginTransaction() {
    wrapped.beginTransaction()
  }

  override fun update(table: String?, conflictAlgorithm: Int, values: ContentValues?, whereClause: String?, whereArgs: Array<out Any>?): Int {
    return profileWithValue { wrapped.update(table, conflictAlgorithm, values, whereClause, whereArgs) }
  }

  override fun isOpen(): Boolean {
    return wrapped.isOpen
  }

  override fun getAttachedDbs(): MutableList<Pair<String, String>> {
    return wrapped.attachedDbs
  }

  override fun getVersion(): Int {
    return wrapped.version
  }

  override fun execSQL(sql: String?) {
    profileWithValue { wrapped.execSQL(sql) }
  }

  override fun execSQL(sql: String?, bindArgs: Array<out Any>?) {
    profileWithValue { wrapped.execSQL(sql, bindArgs) }
  }

  override fun yieldIfContendedSafely(): Boolean {
    return wrapped.yieldIfContendedSafely()
  }

  override fun yieldIfContendedSafely(sleepAfterYieldDelay: Long): Boolean {
    return wrapped.yieldIfContendedSafely(sleepAfterYieldDelay)
  }

  override fun close() {
    wrapped.close()
  }

  override fun delete(table: String?, whereClause: String?, whereArgs: Array<out Any>?): Int {
    return profileWithValue { wrapped.delete(table, whereClause, whereArgs) }
  }

  override fun needUpgrade(newVersion: Int): Boolean {
    return wrapped.needUpgrade(newVersion)
  }

  override fun setMaxSqlCacheSize(cacheSize: Int) {
    wrapped.setMaxSqlCacheSize(cacheSize)
  }

  override fun setForeignKeyConstraintsEnabled(enable: Boolean) {
    wrapped.setForeignKeyConstraintsEnabled(enable)
  }

  override fun beginTransactionNonExclusive() {
    wrapped.beginTransactionNonExclusive()
  }

  override fun setTransactionSuccessful() {
    wrapped.setTransactionSuccessful()
  }

  override fun setVersion(version: Int) {
    wrapped.version = version
  }

  override fun beginTransactionWithListener(transactionListener: SQLiteTransactionListener?) {
    wrapped.beginTransactionWithListener(transactionListener)
  }

  override fun inTransaction(): Boolean {
    return wrapped.inTransaction()
  }

  override fun isReadOnly(): Boolean {
    return wrapped.isReadOnly
  }

  override fun getPath(): String {
    return wrapped.path
  }

  override fun getPageSize(): Long {
    return wrapped.pageSize
  }
}

private class ProfilingSqliteStatement(
    private val wrapped: SupportSQLiteStatement
) : SupportSQLiteStatement {

  override fun bindLong(index: Int, value: Long) {
    wrapped.bindLong(index, value)
  }

  override fun simpleQueryForLong(): Long {
    return profileWithValue { wrapped.simpleQueryForLong() }
  }

  override fun bindString(index: Int, value: String?) {
    wrapped.bindString(index, value)
  }

  override fun bindDouble(index: Int, value: Double) {
    wrapped.bindDouble(index, value)
  }

  override fun simpleQueryForString(): String {
    return profileWithValue { wrapped.simpleQueryForString() }
  }

  override fun clearBindings() {
    wrapped.clearBindings()
  }

  override fun execute() {
    profileWithValue { wrapped.execute() }
  }

  override fun executeInsert(): Long {
    return profileWithValue { wrapped.executeInsert() }
  }

  override fun bindBlob(index: Int, value: ByteArray?) {
    wrapped.bindBlob(index, value)
  }

  override fun executeUpdateDelete(): Int {
    return profileWithValue { wrapped.executeUpdateDelete() }
  }

  override fun close() {
    wrapped.close()
  }

  override fun bindNull(index: Int) {
    wrapped.bindNull(index)
  }
}

private inline fun <T> profileWithValue(block: () -> T): T {
  val stacktrace = Throwable()

  stacktrace.fillInStackTrace()

  val element = stacktrace
      .stackTrace
      .find {
        (it.className.startsWith("org.simple.clinic") && it.className.contains("Dao_Impl"))
            || (it.className.startsWith("androidx.room.InvalidationTracker") && it.methodName in setOf("startTrackingTable", "stopTrackingTable", "checkUpdatedTable"))
      }

  val isInvalidationTrackerQueries = element != null && element.className.startsWith("androidx.room.InvalidationTracker") && element.methodName in setOf("startTrackingTable", "stopTrackingTable", "checkUpdatedTable")

  val now = System.currentTimeMillis()
  val value = block()
  val timeTaken = System.currentTimeMillis() - now

  if (element != null && !isInvalidationTrackerQueries) {
    Timber.tag("db.profile").e(stacktrace)
    Timber.tag("db.profile").d("Class: ${element.className}.${element.methodName}")
    Timber.tag("db.profile").d("Time taken: ${timeTaken}ms")
  } else if (element == null) {
    Timber.tag("db.profile").e(stacktrace)
  }

  return value
}
