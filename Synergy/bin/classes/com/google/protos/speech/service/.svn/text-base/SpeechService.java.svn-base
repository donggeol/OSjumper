package com.google.protos.speech.service;

public final class SpeechService 
{
	public enum Encoding implements com.google.protobuf.Internal.EnumLite 
	{
		ULAW(0, 1), LINEAR_16(1, 2), AMR_NB(2, 4), SPEEX_NB(3, 6), FLAC(4, 7), MP3(5, 8), SPEEX_WB(6, 9), ;

		public static final int ULAW_VALUE = 1;
		public static final int LINEAR_16_VALUE = 2;
		public static final int AMR_NB_VALUE = 4;
		public static final int SPEEX_NB_VALUE = 6;
		public static final int FLAC_VALUE = 7;
		public static final int MP3_VALUE = 8;
		public static final int SPEEX_WB_VALUE = 9;

		public final int getNumber() 
		{
			return value;
		}

		public static Encoding valueOf(int value) 
		{
			switch (value) 
			{
				case 1: return ULAW;
				case 2: return LINEAR_16;
				case 4: return AMR_NB;
				case 6: return SPEEX_NB;
				case 7: return FLAC;
				case 8: return MP3;
				case 9: return SPEEX_WB;
				default: return null;
			}
		}

		public static com.google.protobuf.Internal.EnumLiteMap<Encoding> internalGetValueMap() 
		{
			return internalValueMap;
		}

		private static com.google.protobuf.Internal.EnumLiteMap<Encoding> internalValueMap = new com.google.protobuf.Internal.EnumLiteMap<Encoding>() 
		{
			public Encoding findValueByNumber(int number)
			{
				return Encoding.valueOf(number);
			}
		};

		private final int value;

		private Encoding(int index, int value) 
		{
			this.value = value;
		}
	}
}