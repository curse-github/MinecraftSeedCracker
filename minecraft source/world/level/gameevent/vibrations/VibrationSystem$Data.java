/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
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
/*     */ public final class Data
/*     */ {
/* 161 */   public static Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(VibrationInfo.CODEC
/* 162 */         .lenientOptionalFieldOf("event").forGetter(()), VibrationSelector.CODEC
/* 163 */         .fieldOf("selector").forGetter(Data::getSelectionStrategy), ExtraCodecs.NON_NEGATIVE_INT
/* 164 */         .fieldOf("event_delay").orElse(Integer.valueOf(0)).forGetter(Data::getTravelTimeInTicks))
/* 165 */       .apply(i, ()));
/*     */   
/*     */   public static final String NBT_TAG_KEY = "listener";
/*     */   
/*     */   private VibrationInfo currentVibration;
/*     */   private int travelTimeInTicks;
/*     */   private final VibrationSelector selectionStrategy;
/*     */   private boolean reloadVibrationParticle;
/*     */   
/*     */   private Data(VibrationInfo currentVibration, VibrationSelector selectionStrategy, int travelTimeInTicks, boolean reloadVibrationParticle) {
/* 175 */     this.currentVibration = currentVibration;
/* 176 */     this.travelTimeInTicks = travelTimeInTicks;
/* 177 */     this.selectionStrategy = selectionStrategy;
/* 178 */     this.reloadVibrationParticle = reloadVibrationParticle;
/*     */   }
/*     */ 
/*     */   
/* 182 */   public Data() { this(null, new VibrationSelector(), 0, false); }
/*     */ 
/*     */ 
/*     */   
/* 186 */   public VibrationSelector getSelectionStrategy() { return this.selectionStrategy; }
/*     */ 
/*     */ 
/*     */   
/* 190 */   public VibrationInfo getCurrentVibration() { return this.currentVibration; }
/*     */ 
/*     */ 
/*     */   
/* 194 */   public void setCurrentVibration(VibrationInfo currentVibration) { this.currentVibration = currentVibration; }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public int getTravelTimeInTicks() { return this.travelTimeInTicks; }
/*     */ 
/*     */ 
/*     */   
/* 202 */   public void setTravelTimeInTicks(int travelTimeInTicks) { this.travelTimeInTicks = travelTimeInTicks; }
/*     */ 
/*     */ 
/*     */   
/* 206 */   public void decrementTravelTime() { this.travelTimeInTicks = Math.max(0, this.travelTimeInTicks - 1); }
/*     */ 
/*     */ 
/*     */   
/* 210 */   public boolean shouldReloadVibrationParticle() { return this.reloadVibrationParticle; }
/*     */ 
/*     */ 
/*     */   
/* 214 */   public void setReloadVibrationParticle(boolean reloadVibrationParticle) { this.reloadVibrationParticle = reloadVibrationParticle; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem$Data.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */