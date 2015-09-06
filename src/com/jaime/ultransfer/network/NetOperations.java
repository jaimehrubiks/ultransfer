/* 
 * Copyright (C) 2015 Jaime Hidalgo García
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaime.ultransfer.network;

/**
 *
 * @author Jaime
 */
public class NetOperations {
    
    public static byte PASSWORD_REQUIRED     = 1;
    public static byte PASSWORD_ACK          = 2;
    public static byte PASSWORD_NACK         = 3;
    public static byte FILE_SOON             = 4;
    public static byte FILE_NOT_FOUND        = 5;
    public static byte FILE_ACK              = 6;
    public static byte FILE_NACK             = 7;
    public static byte FILE_EOF              = 8;
    public static byte READY_TO_CLOSE        = 9;
    
    
    
}
