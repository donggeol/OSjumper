package org.synergy.phone.view;

import org.synergy.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class OSJumperPhoneView 
{
	private ImageView mLeftVolume[] = new ImageView[7];
	private ImageView mRightVolume[] = new ImageView[7]; 
	private ImageView mMicrophone; 
	
	private final Drawable mWhiteDrawable;
	private final Drawable mRedDrawable;
	private final Drawable mTransparentDrawable;
	
	private int nSTTMinDecibelStep = 2;
	private int nSTTMaxDecibelStep = 7;

	public OSJumperPhoneView(Context objContext)
	{
		this.mWhiteDrawable = objContext.getResources().getDrawable(R.drawable.decibel_bar_white);
		this.mRedDrawable = objContext.getResources().getDrawable(R.drawable.decibel_bar_yellow);
		this.mTransparentDrawable = objContext.getResources().getDrawable(R.drawable.decibel_bar_transparent);
	}
	
	public void initVTTRLeftVolumeView(ImageView mLeftVolume01, ImageView mLeftVolume02, ImageView mLeftVolume03,
			ImageView mLeftVolume04, ImageView mLeftVolume05, ImageView mLeftVolume06, ImageView mLeftVolume07)
	{
		mLeftVolume[0] = mLeftVolume01;
		mLeftVolume[1] = mLeftVolume02;
		mLeftVolume[2] = mLeftVolume03;
		mLeftVolume[3] = mLeftVolume04;
		mLeftVolume[4] = mLeftVolume05;
		mLeftVolume[5] = mLeftVolume06;
		mLeftVolume[6] = mLeftVolume07;
	}
	
	public void initVTTRRightVolumeView(ImageView mRightVolume01, ImageView mRightVolume02, ImageView mRightVolume03
			,ImageView mRightVolume04, ImageView mRightVolume05, ImageView mRightVolume06, ImageView mRightVolume07)
	{
		mRightVolume[0] = mRightVolume01;
		mRightVolume[1] = mRightVolume02;
		mRightVolume[2] = mRightVolume03;
		mRightVolume[3] = mRightVolume04;
		mRightVolume[4] = mRightVolume05;
		mRightVolume[5] = mRightVolume06;
		mRightVolume[6] = mRightVolume07;
	}
	
	public void initVTRSMicrophoneView(ImageView mMicrophone)
	{
		this.mMicrophone = mMicrophone;
	}
	
	public void initDecibel(int nSTTMinDecibel, int nSTTMaxDecibel)
	{
		this.nSTTMinDecibelStep = (nSTTMinDecibel/10);
		this.nSTTMaxDecibelStep = (nSTTMaxDecibel/10);
	}
	
	public void setVolumeImg(int step)
	{
		for(int i=0; i<7; i++)
		{
			if(i<step)
			{
				if(nSTTMinDecibelStep > step || nSTTMaxDecibelStep < step)
				{
					mLeftVolume[i].setBackground(mRedDrawable);
					mRightVolume[i].setBackground(mRedDrawable);
				}
				else
				{
					mLeftVolume[i].setBackground(mWhiteDrawable);
					mRightVolume[i].setBackground(mWhiteDrawable);
				}
			}
			else
			{
				mLeftVolume[i].setBackground(mTransparentDrawable);
				mRightVolume[i].setBackground(mTransparentDrawable);
			}
		}
	}
}
