/*    */ package net.minecraft.world.item.consume_effects;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TeleportRandomlyConsumeEffect extends Record implements ConsumeEffect {
/*    */   private final float diameter;
/*    */   private static final float DEFAULT_DIAMETER = 16.0F;
/*    */   
/* 22 */   public TeleportRandomlyConsumeEffect(float diameter) { this.diameter = diameter; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 22 */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect; } public float diameter() { return this.diameter; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 24 */   public static final MapCodec<TeleportRandomlyConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.POSITIVE_FLOAT
/* 25 */         .optionalFieldOf("diameter", Float.valueOf(16.0F)).forGetter(TeleportRandomlyConsumeEffect::diameter))
/* 26 */       .apply(i, TeleportRandomlyConsumeEffect::new));
/* 27 */   public static final StreamCodec<RegistryFriendlyByteBuf, TeleportRandomlyConsumeEffect> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, TeleportRandomlyConsumeEffect::diameter, TeleportRandomlyConsumeEffect::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public TeleportRandomlyConsumeEffect() { this(16.0F); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public ConsumeEffect.Type<TeleportRandomlyConsumeEffect> getType() { return ConsumeEffect.Type.TELEPORT_RANDOMLY; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean apply(Level level, ItemStack stack, LivingEntity user) {
/* 43 */     boolean teleported = false;
/* 44 */     for (int attempt = 0; attempt < 16; attempt++) {
/* 45 */       double xx = user.getX() + (user.getRandom().nextDouble() - 0.5D) * this.diameter;
/* 46 */       double yy = Mth.clamp(user.getY() + (user.getRandom().nextDouble() - 0.5D) * this.diameter, level.getMinY(), (level.getMinY() + ((ServerLevel)level).getLogicalHeight() - 1));
/* 47 */       double zz = user.getZ() + (user.getRandom().nextDouble() - 0.5D) * this.diameter;
/* 48 */       if (user.isPassenger()) {
/* 49 */         user.stopRiding();
/*    */       }
/* 51 */       Vec3 oldPos = user.position();
/* 52 */       if (user.randomTeleport(xx, yy, zz, true)) {
/* 53 */         SoundEvent soundEvent; SoundSource soundSource; level.gameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Context.of(user));
/*    */ 
/*    */ 
/*    */         
/* 57 */         if (user instanceof net.minecraft.world.entity.animal.fox.Fox) {
/* 58 */           soundEvent = SoundEvents.FOX_TELEPORT;
/* 59 */           soundSource = SoundSource.NEUTRAL;
/*    */         } else {
/* 61 */           soundEvent = SoundEvents.CHORUS_FRUIT_TELEPORT;
/* 62 */           soundSource = SoundSource.PLAYERS;
/*    */         } 
/* 64 */         level.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, soundSource);
/* 65 */         user.resetFallDistance();
/* 66 */         teleported = true;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 71 */     if (teleported && user instanceof Player) { Player player = (Player)user;
/* 72 */       player.resetCurrentImpulseContext(); }
/*    */ 
/*    */     
/* 75 */     return teleported;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\consume_effects\TeleportRandomlyConsumeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */