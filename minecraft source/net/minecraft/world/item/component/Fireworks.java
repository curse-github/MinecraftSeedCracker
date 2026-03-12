/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class Fireworks extends Record implements TooltipProvider {
/*    */   private final int flightDuration;
/*    */   private final List<FireworkExplosion> explosions;
/*    */   public static final int MAX_EXPLOSIONS = 256;
/*    */   
/* 19 */   public int flightDuration() { return this.flightDuration; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/Fireworks;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Fireworks; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/Fireworks;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Fireworks; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/Fireworks;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/Fireworks;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public List<FireworkExplosion> explosions() { return this.explosions; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static final Codec<Fireworks> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.UNSIGNED_BYTE
/* 27 */         .optionalFieldOf("flight_duration", Integer.valueOf(0)).forGetter(Fireworks::flightDuration), FireworkExplosion.CODEC
/* 28 */         .sizeLimitedListOf(256).optionalFieldOf("explosions", List.of()).forGetter(Fireworks::explosions))
/* 29 */       .apply(i, Fireworks::new));
/*    */   
/* 31 */   public static final StreamCodec<ByteBuf, Fireworks> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Fireworks::flightDuration, FireworkExplosion.STREAM_CODEC
/*    */       
/* 33 */       .apply(ByteBufCodecs.list(256)), Fireworks::explosions, Fireworks::new);
/*    */ 
/*    */ 
/*    */   
/*    */   public Fireworks(int flightDuration, List<FireworkExplosion> explosions) {
/* 38 */     if (explosions.size() > 256)
/* 39 */       throw new IllegalArgumentException("Got " + explosions.size() + " explosions, but maximum is 256"); 
/*    */     this.flightDuration = flightDuration;
/*    */     this.explosions = explosions;
/*    */   }
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 45 */     if (this.flightDuration > 0) {
/* 46 */       consumer.accept(Component.translatable("item.minecraft.firework_rocket.flight").append(CommonComponents.SPACE).append(String.valueOf(this.flightDuration)).withStyle(ChatFormatting.GRAY));
/*    */     }
/*    */     
/* 49 */     FireworkExplosion current = null;
/* 50 */     int count = 0;
/* 51 */     for (FireworkExplosion explosion : this.explosions) {
/* 52 */       if (current == null) {
/* 53 */         current = explosion;
/* 54 */         count = 1; continue;
/* 55 */       }  if (current.equals(explosion)) {
/* 56 */         count++; continue;
/*    */       } 
/* 58 */       addExplosionTooltip(consumer, current, count);
/* 59 */       current = explosion;
/* 60 */       count = 1;
/*    */     } 
/*    */     
/* 63 */     if (current != null) {
/* 64 */       addExplosionTooltip(consumer, current, count);
/*    */     }
/*    */   }
/*    */   
/*    */   private static void addExplosionTooltip(Consumer<Component> consumer, FireworkExplosion explosion, int count) {
/* 69 */     MutableComponent mutableComponent = explosion.shape().getName();
/* 70 */     if (count == 1) {
/* 71 */       consumer.accept(Component.translatable("item.minecraft.firework_rocket.single_star", new Object[] { mutableComponent }).withStyle(ChatFormatting.GRAY));
/*    */     } else {
/* 73 */       consumer.accept(Component.translatable("item.minecraft.firework_rocket.multiple_stars", new Object[] { Integer.valueOf(count), mutableComponent }).withStyle(ChatFormatting.GRAY));
/*    */     } 
/* 75 */     explosion.addAdditionalTooltip(component -> consumer.accept(Component.literal("  ").append(component)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\Fireworks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */