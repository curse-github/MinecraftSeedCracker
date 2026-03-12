/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class CarverDebugSettings {
/*  9 */   public static final CarverDebugSettings DEFAULT = new CarverDebugSettings(false, Blocks.ACACIA_BUTTON
/*    */       
/* 11 */       .defaultBlockState(), Blocks.CANDLE
/* 12 */       .defaultBlockState(), Blocks.ORANGE_STAINED_GLASS
/* 13 */       .defaultBlockState(), Blocks.GLASS
/* 14 */       .defaultBlockState());
/*    */ 
/*    */   
/* 17 */   public static final Codec<CarverDebugSettings> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/* 18 */         .optionalFieldOf("debug_mode", Boolean.valueOf(false)).forGetter(CarverDebugSettings::isDebugMode), BlockState.CODEC
/* 19 */         .optionalFieldOf("air_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getAirState), BlockState.CODEC
/* 20 */         .optionalFieldOf("water_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getWaterState), BlockState.CODEC
/* 21 */         .optionalFieldOf("lava_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getLavaState), BlockState.CODEC
/* 22 */         .optionalFieldOf("barrier_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getBarrierState))
/* 23 */       .apply(i, CarverDebugSettings::new));
/*    */   
/*    */   private final boolean debugMode;
/*    */   
/*    */   private final BlockState airState;
/*    */   private final BlockState waterState;
/*    */   private final BlockState lavaState;
/*    */   private final BlockState barrierState;
/*    */   
/* 32 */   public static CarverDebugSettings of(boolean enabled, BlockState airState, BlockState waterState, BlockState lavaState, BlockState barrierState) { return new CarverDebugSettings(enabled, airState, waterState, lavaState, barrierState); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public static CarverDebugSettings of(BlockState airState, BlockState waterState, BlockState lavaState, BlockState barrierState) { return new CarverDebugSettings(false, airState, waterState, lavaState, barrierState); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public static CarverDebugSettings of(boolean debugMode, BlockState airState) { return new CarverDebugSettings(debugMode, airState, DEFAULT.getWaterState(), DEFAULT.getLavaState(), DEFAULT.getBarrierState()); }
/*    */ 
/*    */   
/*    */   private CarverDebugSettings(boolean debugMode, BlockState airState, BlockState waterState, BlockState lavaState, BlockState barrierState) {
/* 44 */     this.debugMode = debugMode;
/* 45 */     this.airState = airState;
/* 46 */     this.waterState = waterState;
/* 47 */     this.lavaState = lavaState;
/* 48 */     this.barrierState = barrierState;
/*    */   }
/*    */ 
/*    */   
/* 52 */   public boolean isDebugMode() { return this.debugMode; }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public BlockState getAirState() { return this.airState; }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public BlockState getWaterState() { return this.waterState; }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public BlockState getLavaState() { return this.lavaState; }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public BlockState getBarrierState() { return this.barrierState; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\carver\CarverDebugSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */