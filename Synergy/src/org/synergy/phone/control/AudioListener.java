package org.synergy.phone.control;

import java.nio.ByteBuffer;

public interface AudioListener {
	public void onAudioData(ByteBuffer buffer);
}
