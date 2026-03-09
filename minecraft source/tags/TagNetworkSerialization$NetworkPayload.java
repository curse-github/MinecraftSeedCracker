/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NetworkPayload
/*    */ {
/* 58 */   public static final NetworkPayload EMPTY = new NetworkPayload(Map.of());
/*    */   
/*    */   private final Map<Identifier, IntList> tags;
/*    */ 
/*    */   
/* 63 */   NetworkPayload(Map<Identifier, IntList> tags) { this.tags = tags; }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public void write(FriendlyByteBuf buf) { buf.writeMap(this.tags, FriendlyByteBuf::writeIdentifier, FriendlyByteBuf::writeIntIdList); }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public static NetworkPayload read(FriendlyByteBuf buf) { return new NetworkPayload(buf.readMap(FriendlyByteBuf::readIdentifier, FriendlyByteBuf::readIntIdList)); }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public boolean isEmpty() { return this.tags.isEmpty(); }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public int size() { return this.tags.size(); }
/*    */ 
/*    */ 
/*    */   
/* 83 */   public <T> TagLoader.LoadResult<T> resolve(Registry<T> registry) { return TagNetworkSerialization.deserializeTagsFromNetwork(registry, this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\TagNetworkSerialization$NetworkPayload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */