/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Condition
/*     */   extends Record
/*     */ {
/*     */   private final int maxDurationTicks;
/*     */   private final float minSpeed;
/*     */   private final float minRelativeSpeed;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/KineticWeapon$Condition;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #139	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/KineticWeapon$Condition; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/KineticWeapon$Condition;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #139	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/KineticWeapon$Condition; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/KineticWeapon$Condition;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #139	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/KineticWeapon$Condition;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 139 */   public Condition(int maxDurationTicks, float minSpeed, float minRelativeSpeed) { this.maxDurationTicks = maxDurationTicks; this.minSpeed = minSpeed; this.minRelativeSpeed = minRelativeSpeed; } public int maxDurationTicks() { return this.maxDurationTicks; } public float minSpeed() { return this.minSpeed; } public float minRelativeSpeed() { return this.minRelativeSpeed; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public static final Codec<Condition> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/* 145 */         .fieldOf("max_duration_ticks").forGetter(Condition::maxDurationTicks), Codec.FLOAT
/* 146 */         .optionalFieldOf("min_speed", Float.valueOf(0.0F)).forGetter(Condition::minSpeed), Codec.FLOAT
/* 147 */         .optionalFieldOf("min_relative_speed", Float.valueOf(0.0F)).forGetter(Condition::minRelativeSpeed))
/* 148 */       .apply(i, Condition::new));
/*     */   
/* 150 */   public static final StreamCodec<ByteBuf, Condition> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Condition::maxDurationTicks, ByteBufCodecs.FLOAT, Condition::minSpeed, ByteBufCodecs.FLOAT, Condition::minRelativeSpeed, Condition::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   public boolean test(int ticksUsed, double attackerSpeed, double relativeSpeed, double entityFactor) { return (ticksUsed <= this.maxDurationTicks && attackerSpeed >= this.minSpeed * entityFactor && relativeSpeed >= this.minRelativeSpeed * entityFactor); }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public static Optional<Condition> ofAttackerSpeed(int untilTicks, float minAttackerSpeed) { return Optional.of(new Condition(untilTicks, minAttackerSpeed, 0.0F)); }
/*     */ 
/*     */ 
/*     */   
/* 166 */   public static Optional<Condition> ofRelativeSpeed(int untilTicks, float minRelativeSpeed) { return Optional.of(new Condition(untilTicks, 0.0F, minRelativeSpeed)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\KineticWeapon$Condition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */