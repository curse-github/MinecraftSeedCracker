/*    */ package net.minecraft.world.food;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.Consumable;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class FoodProperties extends Record implements ConsumableListener {
/*    */   private final int nutrition;
/*    */   
/* 20 */   public FoodProperties(int nutrition, float saturation, boolean canAlwaysEat) { this.nutrition = nutrition; this.saturation = saturation; this.canAlwaysEat = canAlwaysEat; } private final float saturation; private final boolean canAlwaysEat; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/food/FoodProperties;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/food/FoodProperties; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/food/FoodProperties;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/food/FoodProperties; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/food/FoodProperties;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/food/FoodProperties;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public int nutrition() { return this.nutrition; } public float saturation() { return this.saturation; } public boolean canAlwaysEat() { return this.canAlwaysEat; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static final Codec<FoodProperties> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/* 27 */         .fieldOf("nutrition").forGetter(FoodProperties::nutrition), Codec.FLOAT
/* 28 */         .fieldOf("saturation").forGetter(FoodProperties::saturation), Codec.BOOL
/* 29 */         .optionalFieldOf("can_always_eat", Boolean.valueOf(false)).forGetter(FoodProperties::canAlwaysEat))
/* 30 */       .apply(i, FoodProperties::new));
/*    */   
/* 32 */   public static final StreamCodec<RegistryFriendlyByteBuf, FoodProperties> DIRECT_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, FoodProperties::nutrition, ByteBufCodecs.FLOAT, FoodProperties::saturation, ByteBufCodecs.BOOL, FoodProperties::canAlwaysEat, FoodProperties::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable) {
/* 41 */     RandomSource random = user.getRandom();
/* 42 */     level.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)consumable.sound().value(), SoundSource.NEUTRAL, 1.0F, random.triangle(1.0F, 0.4F));
/*    */     
/* 44 */     if (user instanceof Player) { Player player = (Player)user;
/* 45 */       player.getFoodData().eat(this);
/* 46 */       level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, Mth.randomBetween(random, 0.9F, 1.0F)); }
/*    */   
/*    */   }
/*    */   
/*    */   public static class Builder {
/*    */     private int nutrition;
/*    */     private float saturationModifier;
/*    */     private boolean canAlwaysEat;
/*    */     
/*    */     public Builder nutrition(int nutrition) {
/* 56 */       this.nutrition = nutrition;
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     public Builder saturationModifier(float saturationModifier) {
/* 61 */       this.saturationModifier = saturationModifier;
/* 62 */       return this;
/*    */     }
/*    */     
/*    */     public Builder alwaysEdible() {
/* 66 */       this.canAlwaysEat = true;
/* 67 */       return this;
/*    */     }
/*    */     
/*    */     public FoodProperties build() {
/* 71 */       float saturation = FoodConstants.saturationByModifier(this.nutrition, this.saturationModifier);
/* 72 */       return new FoodProperties(this.nutrition, saturation, this.canAlwaysEat);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\food\FoodProperties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */