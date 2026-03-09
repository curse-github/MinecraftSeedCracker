/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.level.block.entity.FuelValues;
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
/*     */ public class AnyFuel
/*     */   implements SlotDisplay
/*     */ {
/*  97 */   public static final AnyFuel INSTANCE = new AnyFuel();
/*     */   
/*  99 */   public static final MapCodec<AnyFuel> MAP_CODEC = MapCodec.unit(INSTANCE);
/*     */   
/* 101 */   public static final StreamCodec<RegistryFriendlyByteBuf, AnyFuel> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */   
/* 103 */   public static final SlotDisplay.Type<AnyFuel> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public SlotDisplay.Type<AnyFuel> type() { return TYPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public String toString() { return "<any fuel>"; }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 120 */     if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks)factory;
/* 121 */       FuelValues fuelValues = (FuelValues)context.getOptional(SlotDisplayContext.FUEL_VALUES);
/* 122 */       if (fuelValues != null) {
/* 123 */         Objects.requireNonNull(stacks); return fuelValues.fuelItems().stream().map(stacks::forStack);
/*     */       }  }
/*     */     
/* 126 */     return Stream.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$AnyFuel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */