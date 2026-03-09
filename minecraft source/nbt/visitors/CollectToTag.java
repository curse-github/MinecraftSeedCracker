/*     */ package net.minecraft.nbt.visitors;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import net.minecraft.nbt.ByteArrayTag;
/*     */ import net.minecraft.nbt.ByteTag;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.DoubleTag;
/*     */ import net.minecraft.nbt.EndTag;
/*     */ import net.minecraft.nbt.FloatTag;
/*     */ import net.minecraft.nbt.IntArrayTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.LongArrayTag;
/*     */ import net.minecraft.nbt.LongTag;
/*     */ import net.minecraft.nbt.ShortTag;
/*     */ import net.minecraft.nbt.StreamTagVisitor;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagType;
/*     */ 
/*     */ public class CollectToTag
/*     */   implements StreamTagVisitor {
/*     */   public CollectToTag() {
/*  25 */     this.containerStack = new ArrayDeque();
/*     */ 
/*     */     
/*  28 */     this.containerStack.addLast(new RootBuilder());
/*     */   }
/*     */   private final Deque<ContainerBuilder> containerStack;
/*     */   
/*  32 */   public Tag getResult() { return ((ContainerBuilder)this.containerStack.getFirst()).build(); }
/*     */ 
/*     */ 
/*     */   
/*  36 */   protected int depth() { return this.containerStack.size() - 1; }
/*     */ 
/*     */ 
/*     */   
/*  40 */   private void appendEntry(Tag instance) { ((ContainerBuilder)this.containerStack.getLast()).acceptValue(instance); }
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visitEnd() {
/*  45 */     appendEntry(EndTag.INSTANCE);
/*  46 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(String value) {
/*  51 */     appendEntry(StringTag.valueOf(value));
/*  52 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(byte value) {
/*  57 */     appendEntry(ByteTag.valueOf(value));
/*  58 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(short value) {
/*  63 */     appendEntry(ShortTag.valueOf(value));
/*  64 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(int value) {
/*  69 */     appendEntry(IntTag.valueOf(value));
/*  70 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(long value) {
/*  75 */     appendEntry(LongTag.valueOf(value));
/*  76 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(float value) {
/*  81 */     appendEntry(FloatTag.valueOf(value));
/*  82 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(double value) {
/*  87 */     appendEntry(DoubleTag.valueOf(value));
/*  88 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(byte[] value) {
/*  93 */     appendEntry(new ByteArrayTag(value));
/*  94 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(int[] value) {
/*  99 */     appendEntry(new IntArrayTag(value));
/* 100 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(long[] value) {
/* 105 */     appendEntry(new LongArrayTag(value));
/* 106 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public StreamTagVisitor.ValueResult visitList(TagType<?> elementType, int size) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.EntryResult visitElement(TagType<?> type, int index) {
/* 116 */     enterContainerIfNeeded(type);
/* 117 */     return StreamTagVisitor.EntryResult.ENTER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 122 */   public StreamTagVisitor.EntryResult visitEntry(TagType<?> type) { return StreamTagVisitor.EntryResult.ENTER; }
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.EntryResult visitEntry(TagType<?> type, String id) {
/* 127 */     ((ContainerBuilder)this.containerStack.getLast()).acceptKey(id);
/* 128 */     enterContainerIfNeeded(type);
/* 129 */     return StreamTagVisitor.EntryResult.ENTER;
/*     */   }
/*     */   
/*     */   private void enterContainerIfNeeded(TagType<?> type) {
/* 133 */     if (type == ListTag.TYPE) {
/* 134 */       this.containerStack.addLast(new ListBuilder());
/* 135 */     } else if (type == CompoundTag.TYPE) {
/* 136 */       this.containerStack.addLast(new CompoundBuilder());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visitContainerEnd() {
/* 142 */     ContainerBuilder container = (ContainerBuilder)this.containerStack.removeLast();
/* 143 */     Tag tag = container.build();
/* 144 */     if (tag != null) {
/* 145 */       ((ContainerBuilder)this.containerStack.getLast()).acceptValue(tag);
/*     */     }
/* 147 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visitRootEntry(TagType<?> type) {
/* 152 */     enterContainerIfNeeded(type);
/* 153 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */   
/*     */   private static interface ContainerBuilder
/*     */   {
/*     */     default void acceptKey(String id) {}
/*     */     
/*     */     void acceptValue(Tag param1Tag);
/*     */     
/*     */     Tag build();
/*     */   }
/*     */   
/*     */   private static class RootBuilder
/*     */     implements ContainerBuilder
/*     */   {
/*     */     private Tag result;
/*     */     
/* 170 */     public void acceptValue(Tag tag) { this.result = tag; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     public Tag build() { return this.result; }
/*     */   }
/*     */   
/*     */   private static class CompoundBuilder
/*     */     implements ContainerBuilder {
/* 180 */     private final CompoundTag compound = new CompoundTag();
/* 181 */     private String lastId = "";
/*     */ 
/*     */ 
/*     */     
/* 185 */     public void acceptKey(String id) { this.lastId = id; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     public void acceptValue(Tag tag) { this.compound.put(this.lastId, tag); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     public Tag build() { return this.compound; }
/*     */   }
/*     */   
/*     */   private static class ListBuilder
/*     */     implements ContainerBuilder {
/* 200 */     private final ListTag list = new ListTag();
/*     */ 
/*     */ 
/*     */     
/* 204 */     public void acceptValue(Tag tag) { this.list.addAndUnwrap(tag); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     public Tag build() { return this.list; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\visitors\CollectToTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */