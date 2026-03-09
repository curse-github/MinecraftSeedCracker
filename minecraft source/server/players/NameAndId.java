/*    */ package net.minecraft.server.players;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public final class NameAndId extends Record {
/*    */   private final UUID id;
/*    */   private final String name;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/players/NameAndId;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/players/NameAndId; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/players/NameAndId;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/players/NameAndId; }
/*    */   
/* 16 */   public NameAndId(UUID id, String name) { this.id = id; this.name = name; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/players/NameAndId;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/players/NameAndId;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public UUID id() { return this.id; } public String name() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static final Codec<NameAndId> CODEC = RecordCodecBuilder.create(i -> i.group(UUIDUtil.STRING_CODEC
/* 21 */         .fieldOf("id").forGetter(NameAndId::id), Codec.STRING
/* 22 */         .fieldOf("name").forGetter(NameAndId::name))
/* 23 */       .apply(i, NameAndId::new));
/*    */ 
/*    */   
/* 26 */   public NameAndId(GameProfile profile) { this(profile.id(), profile.name()); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public NameAndId(NameAndId profile) { this(profile.id(), profile.name()); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NameAndId fromJson(JsonObject object) {
/*    */     UUID uuid;
/* 38 */     if (!object.has("uuid") || !object.has("name")) {
/* 39 */       return null;
/*    */     }
/* 41 */     String uuidString = object.get("uuid").getAsString();
/*    */     
/*    */     try {
/* 44 */       uuid = UUID.fromString(uuidString);
/* 45 */     } catch (Throwable ignored) {
/* 46 */       return null;
/*    */     } 
/* 48 */     return new NameAndId(uuid, object.get("name").getAsString());
/*    */   }
/*    */   
/*    */   public void appendTo(JsonObject output) {
/* 52 */     output.addProperty("uuid", id().toString());
/* 53 */     output.addProperty("name", name());
/*    */   }
/*    */   
/*    */   public static NameAndId createOffline(String name) {
/* 57 */     UUID id = UUIDUtil.createOfflinePlayerUUID(name);
/* 58 */     return new NameAndId(id, name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\NameAndId.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */