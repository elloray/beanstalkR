package com.github.elloray.beanstalkR.common;

public class ByteUtil {
	public static byte[] int2Bytes(int num) {
		byte[] byteNum = new byte[4];
		for (int i = 0; i < 4; ++i) {
			int offset = 32 - (i + 1) * 8;
			byteNum[i] = (byte) ((num >> offset) & 0xff);
		}
		return byteNum;
	}

	public static int bytes2Int(byte[] byteNum) {
		int num = 0;
		for (int i = 0; i < 4; ++i) {
			num <<= 8;
			num |= (byteNum[i] & 0xff);
		}
		return num;
	}

	public static byte int2OneByte(int num) {
		return (byte) (num & 0x000000ff);
	}

	public static int oneByte2Int(byte byteNum) {
		return byteNum > 0 ? byteNum : (128 + (128 + byteNum));
	}

	public static byte[] long2Bytes(long num) {
		byte[] byteNum = new byte[8];
		for (int i = 0; i < 8; ++i) {
			int offset = 64 - (i + 1) * 8;
			byteNum[i] = (byte) ((num >> offset) & 0xff);
		}
		return byteNum;
	}

	public static long bytes2Long(byte[] byteNum) {
		long num = 0;
		for (int i = 0; i < 8; ++i) {
			num <<= 8;
			num |= (byteNum[i] & 0xff);
		}
		return num;
	}
}
