/*    */ package net.minecraft.server.packs.repository;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class KnownPack extends Record {
/*    */   private final String namespace;
/*    */   private final String id;
/*    */   private final String version;
/*    */   
/* 11 */   public KnownPack(String namespace, String id, String version) { this.namespace = namespace; this.id = id; this.version = version; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/repository/KnownPack;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/server/packs/repository/KnownPack; } public String namespace() { return this.namespace; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/repository/KnownPack;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/repository/KnownPack;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public String id() { return this.id; } public String version() { return this.version; }
/* 12 */   public static final StreamCodec<ByteBuf, KnownPack> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, KnownPack::namespace, ByteBufCodecs.STRING_UTF8, KnownPack::id, ByteBufCodecs.STRING_UTF8, KnownPack::version, KnownPack::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final String VANILLA_NAMESPACE = "minecraft";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static KnownPack vanilla(String id) { return new KnownPack("minecraft", id, SharedConstants.getCurrentVersion().id()); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean isVanilla() { return this.namespace.equals("minecraft"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public String toString() { return this.namespace + ":" + this.namespace + ":" + this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\KnownPack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */