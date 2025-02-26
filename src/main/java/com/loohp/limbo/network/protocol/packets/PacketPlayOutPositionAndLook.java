/*
 * This file is part of Limbo.
 *
 * Copyright (C) 2022. LoohpJames <jamesloohp@gmail.com>
 * Copyright (C) 2022. Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.loohp.limbo.network.protocol.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.loohp.limbo.utils.DataTypeIO;

public class PacketPlayOutPositionAndLook extends PacketOut {
	
	public enum PlayerTeleportFlags {
		X((byte) 0x01),
		Y((byte) 0x02),
		Z((byte) 0x04),
		Y_ROT((byte) 0x08),
		X_ROT((byte) 0x10);
		
		private final byte bit;
		
		PlayerTeleportFlags(byte bit) {
			this.bit = bit;
		}
		
		public byte getBit() {
			return bit;
		}
	}
	
	private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private Set<PlayerTeleportFlags> flags;
    private int teleportId;
    private boolean dismountVehicle;
	
	public PacketPlayOutPositionAndLook(double x, double y, double z, float yaw, float pitch, int teleportId, boolean dismountVehicle, PlayerTeleportFlags... flags) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.teleportId = teleportId;
		this.flags = new HashSet<>(Arrays.asList(flags));
		this.dismountVehicle = dismountVehicle;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public Set<PlayerTeleportFlags> getFlags() {
		return flags;
	}

	public int getTeleportId() {
		return teleportId;
	}
	
	public boolean isDismountVehicle() {
		return dismountVehicle;
	}
	
	@Override
	public byte[] serializePacket() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		DataOutputStream output = new DataOutputStream(buffer);
		output.writeByte(Packet.getPlayOut().get(getClass()));
		output.writeDouble(x);
		output.writeDouble(y);
		output.writeDouble(z);
		output.writeFloat(yaw);
		output.writeFloat(pitch);
		
		byte flag = 0;
		for (PlayerTeleportFlags each : flags) {
			flag = (byte) (flag | each.getBit());
		}
		
		output.writeByte(flag);
		DataTypeIO.writeVarInt(output, teleportId);
		output.writeBoolean(dismountVehicle);
		
		return buffer.toByteArray();
	}

}
