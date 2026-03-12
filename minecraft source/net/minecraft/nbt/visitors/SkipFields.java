/*    */ package net.minecraft.nbt.visitors;
/*    */ 
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.Deque;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.StreamTagVisitor;
/*    */ import net.minecraft.nbt.TagType;
/*    */ 
/*    */ public class SkipFields extends CollectToTag {
/* 10 */   private final Deque<FieldTree> stack = new ArrayDeque();
/*    */   
/*    */   public SkipFields(FieldSelector... wantedFields) {
/* 13 */     FieldTree rootFrame = FieldTree.createRoot();
/* 14 */     for (FieldSelector wantedField : wantedFields) {
/* 15 */       rootFrame.addEntry(wantedField);
/*    */     }
/* 17 */     this.stack.push(rootFrame);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.EntryResult visitEntry(TagType<?> type, String id) {
/* 22 */     FieldTree currentFrame = (FieldTree)this.stack.element();
/* 23 */     if (currentFrame.isSelected(type, id)) {
/* 24 */       return StreamTagVisitor.EntryResult.SKIP;
/*    */     }
/*    */     
/* 27 */     if (type == CompoundTag.TYPE) {
/* 28 */       FieldTree newFrame = (FieldTree)currentFrame.fieldsToRecurse().get(id);
/* 29 */       if (newFrame != null) {
/* 30 */         this.stack.push(newFrame);
/*    */       }
/*    */     } 
/*    */     
/* 34 */     return super.visitEntry(type, id);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.ValueResult visitContainerEnd() {
/* 39 */     if (depth() == ((FieldTree)this.stack.element()).depth()) {
/* 40 */       this.stack.pop();
/*    */     }
/* 42 */     return super.visitContainerEnd();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\visitors\SkipFields.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */