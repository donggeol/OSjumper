/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\duckhyung\\workspace\\ssm\\Synergy\\src\\org\\synergy\\phone\\service\\IRemoteServiceCallback.aidl
 */
package org.synergy.phone.service;
public interface IRemoteServiceCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.synergy.phone.service.IRemoteServiceCallback
{
private static final java.lang.String DESCRIPTOR = "org.synergy.phone.service.IRemoteServiceCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.synergy.phone.service.IRemoteServiceCallback interface,
 * generating a proxy if needed.
 */
public static org.synergy.phone.service.IRemoteServiceCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.synergy.phone.service.IRemoteServiceCallback))) {
return ((org.synergy.phone.service.IRemoteServiceCallback)iin);
}
return new org.synergy.phone.service.IRemoteServiceCallback.Stub.Proxy(obj);
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
case TRANSACTION_onRemoteServiceCallback:
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
this.onRemoteServiceCallback(_arg0, _arg1);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.synergy.phone.service.IRemoteServiceCallback
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
@Override public void onRemoteServiceCallback(int what, android.os.Bundle objBundle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(what);
if ((objBundle!=null)) {
_data.writeInt(1);
objBundle.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onRemoteServiceCallback, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_onRemoteServiceCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onRemoteServiceCallback(int what, android.os.Bundle objBundle) throws android.os.RemoteException;
}
