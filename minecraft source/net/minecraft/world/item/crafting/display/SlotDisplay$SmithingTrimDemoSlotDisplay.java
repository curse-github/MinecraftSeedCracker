/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.SmithingTrimRecipe;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SmithingTrimDemoSlotDisplay
/*     */   extends Record
/*     */   implements SlotDisplay
/*     */ {
/*     */   private final SlotDisplay base;
/*     */   private final SlotDisplay material;
/*     */   private final Holder<TrimPattern> pattern;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #130	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #130	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay; }
/*     */   
/* 130 */   public SmithingTrimDemoSlotDisplay(SlotDisplay base, SlotDisplay material, Holder<TrimPattern> pattern) { this.base = base; this.material = material; this.pattern = pattern; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #130	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;
/* 130 */     //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay base() { return this.base; } public SlotDisplay material() { return this.material; } public Holder<TrimPattern> pattern() { return this.pattern; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   public static final MapCodec<SmithingTrimDemoSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SlotDisplay.CODEC
/* 137 */         .fieldOf("base").forGetter(SmithingTrimDemoSlotDisplay::base), SlotDisplay.CODEC
/* 138 */         .fieldOf("material").forGetter(SmithingTrimDemoSlotDisplay::material), TrimPattern.CODEC
/* 139 */         .fieldOf("pattern").forGetter(SmithingTrimDemoSlotDisplay::pattern))
/* 140 */       .apply(i, SmithingTrimDemoSlotDisplay::new));
/*     */   
/* 142 */   public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTrimDemoSlotDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, SmithingTrimDemoSlotDisplay::base, SlotDisplay.STREAM_CODEC, SmithingTrimDemoSlotDisplay::material, TrimPattern.STREAM_CODEC, SmithingTrimDemoSlotDisplay::pattern, SmithingTrimDemoSlotDisplay::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 149 */   public static final SlotDisplay.Type<SmithingTrimDemoSlotDisplay> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */   
/* 153 */   public SlotDisplay.Type<SmithingTrimDemoSlotDisplay> type() { return TYPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 158 */     if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks)factory;
/* 159 */       HolderLookup.Provider registries = (HolderLookup.Provider)context.getOptional(SlotDisplayContext.REGISTRIES);
/* 160 */       if (registries != null) {
/*     */         
/* 162 */         RandomSource randomSource = RandomSource.create(System.identityHashCode(this));
/* 163 */         List<ItemStack> bases = this.base.resolveForStacks(context);
/* 164 */         if (bases.isEmpty()) {
/* 165 */           return Stream.empty();
/*     */         }
/* 167 */         List<ItemStack> materials = this.material.resolveForStacks(context);
/* 168 */         if (materials.isEmpty()) {
/* 169 */           return Stream.empty();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 178 */         Objects.requireNonNull(stacks); return Stream.generate(() -> { ItemStack base = (ItemStack)Util.getRandom(bases, randomSource); ItemStack material = (ItemStack)Util.getRandom(materials, randomSource); return SmithingTrimRecipe.applyTrim(registries, base, material, this.pattern); }).limit(256L).filter(s -> !s.isEmpty()).limit(16L).map(stacks::forStack);
/*     */       }  }
/*     */     
/* 181 */     return Stream.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$SmithingTrimDemoSlotDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */