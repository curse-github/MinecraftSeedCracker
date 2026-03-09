/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ public final class Criterion<T extends CriterionTriggerInstance> extends Record {
/*    */   private final CriterionTrigger<T> trigger;
/*    */   private final T triggerInstance;
/*    */   
/*  7 */   public Criterion(CriterionTrigger<T> trigger, T triggerInstance) { this.trigger = trigger; this.triggerInstance = triggerInstance; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/Criterion;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/Criterion;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  7 */     //   0	7	0	this	Lnet/minecraft/advancements/Criterion<TT;>; } public CriterionTrigger<T> trigger() { return this.trigger; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/Criterion;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/Criterion;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/Criterion<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/Criterion;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/Criterion;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  7 */     //   0	8	0	this	Lnet/minecraft/advancements/Criterion<TT;>; } public T triggerInstance() { return (T)this.triggerInstance; }
/*  8 */   private static final MapCodec<Criterion<?>> MAP_CODEC = ExtraCodecs.dispatchOptionalValue("trigger", "conditions", CriteriaTriggers.CODEC, Criterion::trigger, Criterion::criterionCodec);
/*  9 */   public static final Codec<Criterion<?>> CODEC = MAP_CODEC.codec();
/*    */ 
/*    */   
/* 12 */   private static <T extends CriterionTriggerInstance> Codec<Criterion<T>> criterionCodec(CriterionTrigger<T> trigger) { return trigger.codec().xmap(instance -> new Criterion(trigger, instance), Criterion::triggerInstance); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\Criterion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */