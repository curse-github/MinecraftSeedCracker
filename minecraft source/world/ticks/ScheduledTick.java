/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ public final class ScheduledTick<T> extends Record {
/*    */   private final T type;
/*    */   private final BlockPos pos;
/*    */   private final long triggerTick;
/*    */   private final TickPriority priority;
/*    */   
/*  9 */   public T type() { return (T)this.type; } private final long subTickOrder; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/ticks/ScheduledTick;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/ScheduledTick;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/ScheduledTick<TT;>; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/ticks/ScheduledTick;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/ScheduledTick;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/ScheduledTick<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/ticks/ScheduledTick;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/ticks/ScheduledTick;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  9 */     //   0	8	0	this	Lnet/minecraft/world/ticks/ScheduledTick<TT;>; } public BlockPos pos() { return this.pos; } public long triggerTick() { return this.triggerTick; } public TickPriority priority() { return this.priority; } public long subTickOrder() { return this.subTickOrder; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Comparator<ScheduledTick<?>> DRAIN_ORDER = (o1, o2) -> {
/* 18 */       int compare = Long.compare(o1.triggerTick, o2.triggerTick);
/* 19 */       if (compare != 0) {
/* 20 */         return compare;
/*    */       }
/* 22 */       compare = o1.priority.compareTo(o2.priority);
/* 23 */       if (compare != 0) {
/* 24 */         return compare;
/*    */       }
/*    */       
/* 27 */       return Long.compare(o1.subTickOrder, o2.subTickOrder);
/*    */     };
/*    */ 
/*    */   
/*    */   public static final Comparator<ScheduledTick<?>> INTRA_TICK_DRAIN_ORDER = (o1, o2) -> {
/* 32 */       int compare = o1.priority.compareTo(o2.priority);
/* 33 */       if (compare != 0) {
/* 34 */         return compare;
/*    */       }
/*    */       
/* 37 */       return Long.compare(o1.subTickOrder, o2.subTickOrder);
/*    */     };
/*    */   
/* 40 */   public static final Hash.Strategy<ScheduledTick<?>> UNIQUE_TICK_HASH = new Hash.Strategy<ScheduledTick<?>>()
/*    */     {
/*    */       public int hashCode(ScheduledTick<?> o) {
/* 43 */         return 31 * o.pos().hashCode() + o.type().hashCode();
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean equals(ScheduledTick<?> a, ScheduledTick<?> b) {
/* 48 */         if (a == b) {
/* 49 */           return true;
/*    */         }
/* 51 */         if (a == null || b == null) {
/* 52 */           return false;
/*    */         }
/* 54 */         return (a.type() == b.type() && a.pos().equals(b.pos()));
/*    */       }
/*    */     };
/*    */ 
/*    */   
/* 59 */   public ScheduledTick(T type, BlockPos pos, long triggerTick, long subTickOrder) { this(type, pos, triggerTick, TickPriority.NORMAL, subTickOrder); }
/*    */   
/*    */   public ScheduledTick(T type, BlockPos pos, long triggerTick, TickPriority priority, long subTickOrder)
/*    */   {
/* 63 */     pos = pos.immutable(); this.type = type;
/*    */     this.pos = pos;
/*    */     this.triggerTick = triggerTick;
/*    */     this.priority = priority;
/* 67 */     this.subTickOrder = subTickOrder; } public static <T> ScheduledTick<T> probe(T type, BlockPos pos) { return new ScheduledTick(type, pos, 0L, TickPriority.NORMAL, 0L); }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public SavedTick<T> toSavedTick(long currentTick) { return new SavedTick(this.type, this.pos, (int)(this.triggerTick - currentTick), this.priority); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\ScheduledTick.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */