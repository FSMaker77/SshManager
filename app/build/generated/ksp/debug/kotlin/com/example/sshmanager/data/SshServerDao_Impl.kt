package com.example.sshmanager.`data`

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SshServerDao_Impl(
  __db: RoomDatabase,
) : SshServerDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSshServer: EntityInsertAdapter<SshServer>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfSshServer: EntityDeleteOrUpdateAdapter<SshServer>

  private val __updateAdapterOfSshServer: EntityDeleteOrUpdateAdapter<SshServer>
  init {
    this.__db = __db
    this.__insertAdapterOfSshServer = object : EntityInsertAdapter<SshServer>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `ssh_servers` (`id`,`name`,`host`,`port`,`username`,`authType`,`password`,`privateKey`,`passphrase`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SshServer) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.host)
        statement.bindLong(4, entity.port.toLong())
        statement.bindText(5, entity.username)
        val _tmp: String = __converters.fromAuthType(entity.authType)
        statement.bindText(6, _tmp)
        val _tmpPassword: String? = entity.password
        if (_tmpPassword == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpPassword)
        }
        val _tmpPrivateKey: String? = entity.privateKey
        if (_tmpPrivateKey == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpPrivateKey)
        }
        val _tmpPassphrase: String? = entity.passphrase
        if (_tmpPassphrase == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpPassphrase)
        }
      }
    }
    this.__deleteAdapterOfSshServer = object : EntityDeleteOrUpdateAdapter<SshServer>() {
      protected override fun createQuery(): String = "DELETE FROM `ssh_servers` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SshServer) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfSshServer = object : EntityDeleteOrUpdateAdapter<SshServer>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `ssh_servers` SET `id` = ?,`name` = ?,`host` = ?,`port` = ?,`username` = ?,`authType` = ?,`password` = ?,`privateKey` = ?,`passphrase` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SshServer) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.host)
        statement.bindLong(4, entity.port.toLong())
        statement.bindText(5, entity.username)
        val _tmp: String = __converters.fromAuthType(entity.authType)
        statement.bindText(6, _tmp)
        val _tmpPassword: String? = entity.password
        if (_tmpPassword == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpPassword)
        }
        val _tmpPrivateKey: String? = entity.privateKey
        if (_tmpPrivateKey == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpPrivateKey)
        }
        val _tmpPassphrase: String? = entity.passphrase
        if (_tmpPassphrase == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpPassphrase)
        }
        statement.bindLong(10, entity.id)
      }
    }
  }

  public override suspend fun insert(server: SshServer): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfSshServer.insertAndReturnId(_connection, server)
    _result
  }

  public override suspend fun delete(server: SshServer): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfSshServer.handle(_connection, server)
  }

  public override suspend fun update(server: SshServer): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfSshServer.handle(_connection, server)
  }

  public override fun observeAll(): Flow<List<SshServer>> {
    val _sql: String = "SELECT * FROM ssh_servers ORDER BY name COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("ssh_servers")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfHost: Int = getColumnIndexOrThrow(_stmt, "host")
        val _columnIndexOfPort: Int = getColumnIndexOrThrow(_stmt, "port")
        val _columnIndexOfUsername: Int = getColumnIndexOrThrow(_stmt, "username")
        val _columnIndexOfAuthType: Int = getColumnIndexOrThrow(_stmt, "authType")
        val _columnIndexOfPassword: Int = getColumnIndexOrThrow(_stmt, "password")
        val _columnIndexOfPrivateKey: Int = getColumnIndexOrThrow(_stmt, "privateKey")
        val _columnIndexOfPassphrase: Int = getColumnIndexOrThrow(_stmt, "passphrase")
        val _result: MutableList<SshServer> = mutableListOf()
        while (_stmt.step()) {
          val _item: SshServer
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpHost: String
          _tmpHost = _stmt.getText(_columnIndexOfHost)
          val _tmpPort: Int
          _tmpPort = _stmt.getLong(_columnIndexOfPort).toInt()
          val _tmpUsername: String
          _tmpUsername = _stmt.getText(_columnIndexOfUsername)
          val _tmpAuthType: AuthType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfAuthType)
          _tmpAuthType = __converters.toAuthType(_tmp)
          val _tmpPassword: String?
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _tmpPassword = null
          } else {
            _tmpPassword = _stmt.getText(_columnIndexOfPassword)
          }
          val _tmpPrivateKey: String?
          if (_stmt.isNull(_columnIndexOfPrivateKey)) {
            _tmpPrivateKey = null
          } else {
            _tmpPrivateKey = _stmt.getText(_columnIndexOfPrivateKey)
          }
          val _tmpPassphrase: String?
          if (_stmt.isNull(_columnIndexOfPassphrase)) {
            _tmpPassphrase = null
          } else {
            _tmpPassphrase = _stmt.getText(_columnIndexOfPassphrase)
          }
          _item = SshServer(_tmpId,_tmpName,_tmpHost,_tmpPort,_tmpUsername,_tmpAuthType,_tmpPassword,_tmpPrivateKey,_tmpPassphrase)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: Long): SshServer? {
    val _sql: String = "SELECT * FROM ssh_servers WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfHost: Int = getColumnIndexOrThrow(_stmt, "host")
        val _columnIndexOfPort: Int = getColumnIndexOrThrow(_stmt, "port")
        val _columnIndexOfUsername: Int = getColumnIndexOrThrow(_stmt, "username")
        val _columnIndexOfAuthType: Int = getColumnIndexOrThrow(_stmt, "authType")
        val _columnIndexOfPassword: Int = getColumnIndexOrThrow(_stmt, "password")
        val _columnIndexOfPrivateKey: Int = getColumnIndexOrThrow(_stmt, "privateKey")
        val _columnIndexOfPassphrase: Int = getColumnIndexOrThrow(_stmt, "passphrase")
        val _result: SshServer?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpHost: String
          _tmpHost = _stmt.getText(_columnIndexOfHost)
          val _tmpPort: Int
          _tmpPort = _stmt.getLong(_columnIndexOfPort).toInt()
          val _tmpUsername: String
          _tmpUsername = _stmt.getText(_columnIndexOfUsername)
          val _tmpAuthType: AuthType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfAuthType)
          _tmpAuthType = __converters.toAuthType(_tmp)
          val _tmpPassword: String?
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _tmpPassword = null
          } else {
            _tmpPassword = _stmt.getText(_columnIndexOfPassword)
          }
          val _tmpPrivateKey: String?
          if (_stmt.isNull(_columnIndexOfPrivateKey)) {
            _tmpPrivateKey = null
          } else {
            _tmpPrivateKey = _stmt.getText(_columnIndexOfPrivateKey)
          }
          val _tmpPassphrase: String?
          if (_stmt.isNull(_columnIndexOfPassphrase)) {
            _tmpPassphrase = null
          } else {
            _tmpPassphrase = _stmt.getText(_columnIndexOfPassphrase)
          }
          _result = SshServer(_tmpId,_tmpName,_tmpHost,_tmpPort,_tmpUsername,_tmpAuthType,_tmpPassword,_tmpPrivateKey,_tmpPassphrase)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
