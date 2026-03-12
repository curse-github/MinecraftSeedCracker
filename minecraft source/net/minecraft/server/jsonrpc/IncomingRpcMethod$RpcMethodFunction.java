package net.minecraft.server.jsonrpc;

import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;

@FunctionalInterface
public interface RpcMethodFunction<Params, Result> {
  Result apply(MinecraftApi paramMinecraftApi, Params paramParams, ClientInfo paramClientInfo);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod$RpcMethodFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */