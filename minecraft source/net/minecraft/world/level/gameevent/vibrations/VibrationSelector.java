/*    */ package net.minecraft.world.level.gameevent.vibrations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import org.apache.commons.lang3.tuple.Pair;
/*    */ 
/*    */ public class VibrationSelector {
/* 10 */   public static final Codec<VibrationSelector> CODEC = RecordCodecBuilder.create(i -> i.group(VibrationInfo.CODEC
/* 11 */         .lenientOptionalFieldOf("event").forGetter(()), Codec.LONG
/* 12 */         .fieldOf("tick").forGetter(()))
/* 13 */       .apply(i, VibrationSelector::new));
/*    */   
/*    */   private Optional<Pair<VibrationInfo, Long>> currentVibrationData;
/*    */ 
/*    */   
/* 18 */   public VibrationSelector(Optional<VibrationInfo> currentVibration, long tick) { this.currentVibrationData = currentVibration.map(vibrationInfo -> Pair.of(vibrationInfo, Long.valueOf(tick))); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public VibrationSelector() { this.currentVibrationData = Optional.empty(); }
/*    */ 
/*    */   
/*    */   public void addCandidate(VibrationInfo newVibration, long tickTime) {
/* 26 */     if (shouldReplaceVibration(newVibration, tickTime)) {
/* 27 */       this.currentVibrationData = Optional.of(Pair.of(newVibration, Long.valueOf(tickTime)));
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean shouldReplaceVibration(VibrationInfo newVibration, long tickTime) {
/* 32 */     if (this.currentVibrationData.isEmpty()) {
/* 33 */       return true;
/*    */     }
/* 35 */     Pair<VibrationInfo, Long> previousData = (Pair)this.currentVibrationData.get();
/* 36 */     long previousTick = ((Long)previousData.getRight()).longValue();
/* 37 */     if (tickTime != previousTick)
/*    */     {
/* 39 */       return false;
/*    */     }
/* 41 */     VibrationInfo previousVibration = (VibrationInfo)previousData.getLeft();
/* 42 */     if (newVibration.distance() < previousVibration.distance())
/* 43 */       return true; 
/* 44 */     if (newVibration.distance() > previousVibration.distance()) {
/* 45 */       return false;
/*    */     }
/* 47 */     return (VibrationSystem.getGameEventFrequency(newVibration.gameEvent()) > VibrationSystem.getGameEventFrequency(previousVibration.gameEvent()));
/*    */   }
/*    */   
/*    */   public Optional<VibrationInfo> chosenCandidate(long time) {
/* 51 */     if (this.currentVibrationData.isEmpty()) {
/* 52 */       return Optional.empty();
/*    */     }
/* 54 */     if (((Long)((Pair)this.currentVibrationData.get()).getRight()).longValue() < time) {
/* 55 */       return Optional.of((VibrationInfo)((Pair)this.currentVibrationData.get()).getLeft());
/*    */     }
/* 57 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/* 61 */   public void startOver() { this.currentVibrationData = Optional.empty(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */