package com.kvxd.tormod.utils

import oshi.SystemInfo
import oshi.software.os.InternetProtocolStats.TcpState
import oshi.software.os.OperatingSystem

object OSUtils {

    private val systemInfo = SystemInfo()
    val os: OperatingSystem = systemInfo.operatingSystem

    /**
     * Returns true if a TCP connection is found listening on the specified port.
     */
    fun isPortInUse(port: Int): Boolean {
        val tcpConnections = os.internetProtocolStats.connections
        return tcpConnections.any { it.localPort == port && it.state == TcpState.LISTEN }
    }

    /**
     * Terminates the process that is listening on the specified port.
     */
    fun terminateProcessOnPort(port: Int) {
        val tcpConnections = os.internetProtocolStats.connections
        val connection = tcpConnections.firstOrNull { it.localPort == port && it.state == TcpState.LISTEN }
        if (connection != null) {
            ProcessHandle.of(connection.getowningProcessId().toLong()).ifPresent { processHandle ->
                processHandle.destroyForcibly()
            }
        }
    }

}