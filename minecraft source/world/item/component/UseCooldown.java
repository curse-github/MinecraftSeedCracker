/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class UseCooldown extends Record {
/*    */   private final float seconds;
/*    */   private final Optional<Identifier> cooldownGroup;
/*    */   
/* 17 */   public UseCooldown(float seconds, Optional<Identifier> cooldownGroup) { this.seconds = seconds; this.cooldownGroup = cooldownGroup; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/UseCooldown;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseCooldown; } public float seconds() { return this.seconds; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/UseCooldown;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseCooldown; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/UseCooldown;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/UseCooldown;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Identifier> cooldownGroup() { return this.cooldownGroup; }
/* 18 */   public static final Codec<UseCooldown> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.POSITIVE_FLOAT
/* 19 */         .fieldOf("seconds").forGetter(UseCooldown::seconds), Identifier.CODEC
/* 20 */         .optionalFieldOf("cooldown_group").forGetter(UseCooldown::cooldownGroup))
/* 21 */       .apply(i, UseCooldown::new));
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, UseCooldown> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, UseCooldown::seconds, Identifier.STREAM_CODEC
/*    */       
/* 24 */       .apply(ByteBufCodecs::optional), UseCooldown::cooldownGroup, UseCooldown::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public UseCooldown(float seconds) { this(seconds, Optional.empty()); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public int ticks() { return (int)(this.seconds * 20.0F); }
/*    */ 
/*    */   
/*    */   public void apply(ItemStack stack, LivingEntity user) {
/* 37 */     if (user instanceof Player) { Player player = (Player)user;
/* 38 */       player.getCooldowns().addCooldown(stack, ticks()); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\UseCooldown.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */