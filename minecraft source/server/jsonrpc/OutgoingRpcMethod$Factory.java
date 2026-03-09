package net.minecraft.server.jsonrpc;

import net.minecraft.server.jsonrpc.api.MethodInfo;

@FunctionalInterface
public interface Factory<Params, Result> {
  OutgoingRpcMethod<Params, Result> create(MethodInfo<Params, Result> paramMethodInfo, OutgoingRpcMethod.Attributes paramAttributes);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */