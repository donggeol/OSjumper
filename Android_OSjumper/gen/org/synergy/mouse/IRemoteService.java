/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\duckhyung\\workspace\\ssm\\Synergy\\src\\org\\synergy\\mouse\\IRemoteService.aidl
 */
package org.synergy.mouse;
public interface IRemoteService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.synergy.mouse.IRemoteService
{
private static final java.lang.String DESCRIPTOR = "org.synergy.mouse.IRemoteService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.synergy.mouse.IRemoteService interface,
 * generating a proxy if needed.
 */
public static org.synergy.mouse.IRemoteService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.synergy.mouse.IRemoteService))) {
return ((org.synergy.mouse.IRemoteService)iin);
}
return new org.synergy.mouse.IRemoteService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
org.synergy.mouse.IRemoteServiceCallback _arg0;
_arg0 = org.synergy.mouse.IRemoteServiceCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterCallback:
{
data.enforceInterface(DESCRIPTOR);
org.synergy.mouse.IRemoteServiceCallback _arg0;
_arg0 = org.synergy.mouse.IRemoteServiceCallback.Stub.asInterface(data.readStrongBinder());
this.unregisterCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onRemoteService:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
android.os.Bundle _arg3;
if ((0!=data.readInt())) {
_arg3 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg3 = null;
}
java.lang.String _result = this.onRemoteService(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_onRemoteClipBoardService:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.os.Bundle _arg1;
if ((0!=data.readInt())) {
_arg1 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _result = this.onRemoteClipBoardService(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_onRemoteKeyBoardService:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
android.os.Bundle _arg2;
if ((0!=data.readInt())) {
_arg2 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
java.lang.String _result = this.onRemoteKeyBoardService(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.synergy.mouse.IRemoteService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void registerCallback(org.synergy.mouse.IRemoteServiceCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterCallback(org.synergy.mouse.IRemoteServiceCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String onRemoteService(int type, int x, int y, android.os.Bundle objBundle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
_data.writeInt(x);
_data.writeInt(y);
if ((objBundle!=null)) {
_data.writeInt(1);
objBundle.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onRemoteService, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String onRemoteClipBoardService(int type, android.os.Bundle objBundle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
if ((objBundle!=null)) {
_data.writeInt(1);
objBundle.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onRemoteClipBoardService, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String onRemoteKeyBoardService(int type, int buttonID, android.os.Bundle objBundle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
_data.writeInt(buttonID);
if ((objBundle!=null)) {
_data.writeInt(1);
objBundle.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onRemoteKeyBoardService, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onRemoteService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onRemoteClipBoardService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onRemoteKeyBoardService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public void registerCallback(org.synergy.mouse.IRemoteServiceCallback cb) throws android.os.RemoteException;
public void unregisterCallback(org.synergy.mouse.IRemoteServiceCallback cb) throws android.os.RemoteException;
public java.lang.String onRemoteService(int type, int x, int y, android.os.Bundle objBundle) throws android.os.RemoteException;
public java.lang.String onRemoteClipBoardService(int type, android.os.Bundle objBundle) throws android.os.RemoteException;
public java.lang.String onRemoteKeyBoardService(int type, int buttonID, android.os.Bundle objBundle) throws android.os.RemoteException;
}
