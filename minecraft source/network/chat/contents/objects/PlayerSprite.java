/*    */ package net.minecraft.network.chat.contents.objects;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ 
/*    */ public final class PlayerSprite extends Record implements ObjectInfo {
/*    */   private final ResolvableProfile player;
/*    */   private final boolean hat;
/*    */   
/*  9 */   public PlayerSprite(ResolvableProfile player, boolean hat) { this.player = player; this.hat = hat; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/objects/PlayerSprite;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/objects/PlayerSprite; } public ResolvableProfile player() { return this.player; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/objects/PlayerSprite;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/objects/PlayerSprite; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/objects/PlayerSprite;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/objects/PlayerSprite;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public boolean hat() { return this.hat; }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static final MapCodec<PlayerSprite> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ResolvableProfile.CODEC
/* 14 */         .fieldOf("player").forGetter(PlayerSprite::player), Codec.BOOL
/* 15 */         .optionalFieldOf("hat", Boolean.valueOf(true)).forGetter(PlayerSprite::hat))
/* 16 */       .apply(i, PlayerSprite::new));
/*    */ 
/*    */ 
/*    */   
/* 20 */   public FontDescription fontDescription() { return new FontDescription.PlayerSprite(this.player, this.hat); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public String description() { return (String)this.player.name().map(name -> "[" + name + " head]").orElse("[unknown player head]"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public MapCodec<PlayerSprite> codec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\objects\PlayerSprite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */