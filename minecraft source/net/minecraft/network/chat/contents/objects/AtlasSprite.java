/*    */ package net.minecraft.network.chat.contents.objects;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class AtlasSprite extends Record implements ObjectInfo {
/*    */   private final Identifier atlas;
/*    */   private final Identifier sprite;
/*    */   
/*  9 */   public AtlasSprite(Identifier atlas, Identifier sprite) { this.atlas = atlas; this.sprite = sprite; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/objects/AtlasSprite;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/objects/AtlasSprite; } public Identifier atlas() { return this.atlas; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/objects/AtlasSprite;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/objects/AtlasSprite; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/objects/AtlasSprite;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/objects/AtlasSprite;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public Identifier sprite() { return this.sprite; }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static final Identifier DEFAULT_ATLAS = AtlasIds.BLOCKS;
/*    */   
/* 15 */   public static final MapCodec<AtlasSprite> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 16 */         .optionalFieldOf("atlas", DEFAULT_ATLAS).forGetter(AtlasSprite::atlas), Identifier.CODEC
/* 17 */         .fieldOf("sprite").forGetter(AtlasSprite::sprite))
/* 18 */       .apply(i, AtlasSprite::new));
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<AtlasSprite> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public FontDescription fontDescription() { return new FontDescription.AtlasSprite(this.atlas, this.sprite); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   private static String toShortName(Identifier id) { return id.getNamespace().equals("minecraft") ? id.getPath() : id.toString(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String description() {
/* 37 */     String shortName = toShortName(this.sprite);
/* 38 */     return this.atlas.equals(DEFAULT_ATLAS) ? ("[" + 
/* 39 */       shortName + "]") : ("[" + 
/*    */       
/* 41 */       shortName + "@" + toShortName(this.atlas) + "]");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\objects\AtlasSprite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */