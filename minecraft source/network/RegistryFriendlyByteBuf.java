/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ 
/*    */ public class RegistryFriendlyByteBuf
/*    */   extends FriendlyByteBuf {
/*    */   private final RegistryAccess registryAccess;
/*    */   
/*    */   public RegistryFriendlyByteBuf(ByteBuf source, RegistryAccess registryAccess) {
/* 12 */     super(source);
/* 13 */     this.registryAccess = registryAccess;
/*    */   }
/*    */ 
/*    */   
/* 17 */   public RegistryAccess registryAccess() { return this.registryAccess; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static Function<ByteBuf, RegistryFriendlyByteBuf> decorator(RegistryAccess registryAccess) { return buf -> new RegistryFriendlyByteBuf(buf, registryAccess); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\RegistryFriendlyByteBuf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */