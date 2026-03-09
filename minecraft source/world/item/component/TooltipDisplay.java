/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceSortedSets;
/*    */ import java.util.SequencedSet;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class TooltipDisplay extends Record {
/*    */   private final boolean hideTooltip;
/*    */   private final SequencedSet<DataComponentType<?>> hiddenComponents;
/*    */   
/* 15 */   public TooltipDisplay(boolean hideTooltip, SequencedSet<DataComponentType<?>> hiddenComponents) { this.hideTooltip = hideTooltip; this.hiddenComponents = hiddenComponents; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/TooltipDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/item/component/TooltipDisplay; } public boolean hideTooltip() { return this.hideTooltip; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/TooltipDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/TooltipDisplay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/TooltipDisplay;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public SequencedSet<DataComponentType<?>> hiddenComponents() { return this.hiddenComponents; }
/*    */   
/* 17 */   private static final Codec<SequencedSet<DataComponentType<?>>> COMPONENT_SET_CODEC = DataComponentType.CODEC.listOf()
/* 18 */     .xmap(ReferenceLinkedOpenHashSet::new, List::copyOf);
/*    */   
/* 20 */   public static final Codec<TooltipDisplay> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/* 21 */         .optionalFieldOf("hide_tooltip", Boolean.valueOf(false)).forGetter(TooltipDisplay::hideTooltip), COMPONENT_SET_CODEC
/* 22 */         .optionalFieldOf("hidden_components", ReferenceSortedSets.emptySet()).forGetter(TooltipDisplay::hiddenComponents))
/* 23 */       .apply(i, TooltipDisplay::new));
/*    */   
/* 25 */   public static final StreamCodec<RegistryFriendlyByteBuf, TooltipDisplay> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, TooltipDisplay::hideTooltip, DataComponentType.STREAM_CODEC
/*    */       
/* 27 */       .apply(ByteBufCodecs.collection(ReferenceLinkedOpenHashSet::new)), TooltipDisplay::hiddenComponents, TooltipDisplay::new);
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final TooltipDisplay DEFAULT = new TooltipDisplay(false, ReferenceSortedSets.emptySet());
/*    */   
/*    */   public TooltipDisplay withHidden(DataComponentType<?> component, boolean hidden) {
/* 34 */     if (this.hiddenComponents.contains(component) == hidden) {
/* 35 */       return this;
/*    */     }
/* 37 */     ReferenceLinkedOpenHashSet referenceLinkedOpenHashSet = new ReferenceLinkedOpenHashSet(this.hiddenComponents);
/* 38 */     if (hidden) {
/* 39 */       referenceLinkedOpenHashSet.add(component);
/*    */     } else {
/* 41 */       referenceLinkedOpenHashSet.remove(component);
/*    */     } 
/* 43 */     return new TooltipDisplay(this.hideTooltip, referenceLinkedOpenHashSet);
/*    */   }
/*    */ 
/*    */   
/* 47 */   public boolean shows(DataComponentType<?> component) { return (!this.hideTooltip && !this.hiddenComponents.contains(component)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\TooltipDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */