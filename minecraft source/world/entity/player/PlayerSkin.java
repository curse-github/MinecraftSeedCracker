/*    */ package net.minecraft.world.entity.player;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ 
/*    */ public final class PlayerSkin extends Record {
/*    */   private final ClientAsset.Texture body;
/*    */   private final ClientAsset.Texture cape;
/*    */   private final ClientAsset.Texture elytra;
/*    */   private final PlayerModelType model;
/*    */   private final boolean secure;
/*    */   
/* 14 */   public PlayerSkin(ClientAsset.Texture body, ClientAsset.Texture cape, ClientAsset.Texture elytra, PlayerModelType model, boolean secure) { this.body = body; this.cape = cape; this.elytra = elytra; this.model = model; this.secure = secure; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/PlayerSkin;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/entity/player/PlayerSkin; } public ClientAsset.Texture body() { return this.body; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/PlayerSkin;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/PlayerSkin; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/PlayerSkin;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/player/PlayerSkin;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public ClientAsset.Texture cape() { return this.cape; } public ClientAsset.Texture elytra() { return this.elytra; } public PlayerModelType model() { return this.model; } public boolean secure() { return this.secure; }
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
/* 28 */   public static PlayerSkin insecure(ClientAsset.Texture body, ClientAsset.Texture cape, ClientAsset.Texture elytra, PlayerModelType model) { return new PlayerSkin(body, cape, elytra, model, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PlayerSkin with(Patch patch) {
/* 38 */     if (patch.equals(Patch.EMPTY)) {
/* 39 */       return this;
/*    */     }
/*    */     
/* 42 */     return insecure(
/* 43 */         (ClientAsset.Texture)DataFixUtils.orElse(patch.body, this.body), 
/* 44 */         (ClientAsset.Texture)DataFixUtils.orElse(patch.cape, this.cape), 
/* 45 */         (ClientAsset.Texture)DataFixUtils.orElse(patch.elytra, this.elytra), (PlayerModelType)patch.model
/* 46 */         .orElse(this.model));
/*    */   }
/*    */   public static final class Patch extends Record { private final Optional<ClientAsset.ResourceTexture> body; private final Optional<ClientAsset.ResourceTexture> cape; private final Optional<ClientAsset.ResourceTexture> elytra; private final Optional<PlayerModelType> model;
/*    */     
/* 50 */     public Patch(Optional<ClientAsset.ResourceTexture> body, Optional<ClientAsset.ResourceTexture> cape, Optional<ClientAsset.ResourceTexture> elytra, Optional<PlayerModelType> model) { this.body = body; this.cape = cape; this.elytra = elytra; this.model = model; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/PlayerSkin$Patch;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/player/PlayerSkin$Patch; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/PlayerSkin$Patch;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/player/PlayerSkin$Patch; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/PlayerSkin$Patch;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/player/PlayerSkin$Patch;
/* 50 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ClientAsset.ResourceTexture> body() { return this.body; } public Optional<ClientAsset.ResourceTexture> cape() { return this.cape; } public Optional<ClientAsset.ResourceTexture> elytra() { return this.elytra; } public Optional<PlayerModelType> model() { return this.model; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 56 */     public static final Patch EMPTY = new Patch(
/* 57 */         Optional.empty(), 
/* 58 */         Optional.empty(), 
/* 59 */         Optional.empty(), 
/* 60 */         Optional.empty());
/*    */ 
/*    */     
/* 63 */     public static final MapCodec<Patch> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ClientAsset.ResourceTexture.CODEC
/* 64 */           .optionalFieldOf("texture").forGetter(Patch::body), ClientAsset.ResourceTexture.CODEC
/* 65 */           .optionalFieldOf("cape").forGetter(Patch::cape), ClientAsset.ResourceTexture.CODEC
/* 66 */           .optionalFieldOf("elytra").forGetter(Patch::elytra), PlayerModelType.CODEC
/* 67 */           .optionalFieldOf("model").forGetter(Patch::model))
/* 68 */         .apply(i, Patch::create));
/*    */     
/* 70 */     public static final StreamCodec<ByteBuf, Patch> STREAM_CODEC = StreamCodec.composite(ClientAsset.ResourceTexture.STREAM_CODEC
/* 71 */         .apply(ByteBufCodecs::optional), Patch::body, ClientAsset.ResourceTexture.STREAM_CODEC
/* 72 */         .apply(ByteBufCodecs::optional), Patch::cape, ClientAsset.ResourceTexture.STREAM_CODEC
/* 73 */         .apply(ByteBufCodecs::optional), Patch::elytra, PlayerModelType.STREAM_CODEC
/* 74 */         .apply(ByteBufCodecs::optional), Patch::model, Patch::create);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static Patch create(Optional<ClientAsset.ResourceTexture> texture, Optional<ClientAsset.ResourceTexture> capeTexture, Optional<ClientAsset.ResourceTexture> elytraTexture, Optional<PlayerModelType> model) {
/* 84 */       if (texture.isEmpty() && capeTexture.isEmpty() && elytraTexture.isEmpty() && model.isEmpty()) {
/* 85 */         return EMPTY;
/*    */       }
/* 87 */       return new Patch(texture, capeTexture, elytraTexture, model);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\PlayerSkin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */