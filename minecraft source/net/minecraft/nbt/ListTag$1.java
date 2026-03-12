/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends Object
/*     */   implements TagType.VariableSize<ListTag>
/*     */ {
/*     */   public ListTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  32 */     accounter.pushDepth();
/*     */     try {
/*  34 */       return loadList(input, accounter);
/*     */     } finally {
/*  36 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ListTag loadList(DataInput input, NbtAccounter accounter) throws IOException {
/*  41 */     accounter.accountBytes(36L);
/*  42 */     byte typeId = input.readByte();
/*  43 */     int count = readListCount(input);
/*  44 */     if (typeId == 0 && count > 0) {
/*  45 */       throw new NbtFormatException("Missing type on ListTag");
/*     */     }
/*  47 */     accounter.accountBytes(4L, count);
/*  48 */     TagType<?> type = TagTypes.getType(typeId);
/*  49 */     ListTag list = new ListTag(new ArrayList(count));
/*  50 */     for (int i = 0; i < count; i++) {
/*  51 */       list.addAndUnwrap(type.load(input, accounter));
/*     */     }
/*  53 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  58 */     accounter.pushDepth();
/*     */     try {
/*  60 */       return parseList(input, output, accounter);
/*     */     } finally {
/*  62 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static StreamTagVisitor.ValueResult parseList(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  67 */     accounter.accountBytes(36L);
/*  68 */     TagType<?> elementType = TagTypes.getType(input.readByte());
/*  69 */     int count = readListCount(input);
/*  70 */     switch (ListTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$ValueResult[output.visitList(elementType, count).ordinal()]) {
/*     */       case 1:
/*  72 */         return StreamTagVisitor.ValueResult.HALT;
/*     */       case 2:
/*  74 */         elementType.skip(input, count, accounter);
/*  75 */         return output.visitContainerEnd();
/*     */     } 
/*     */     
/*  78 */     accounter.accountBytes(4L, count);
/*     */     
/*     */     int i;
/*  81 */     for (i = 0; i < count; i++) {
/*  82 */       switch (ListTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$EntryResult[output.visitElement(elementType, i).ordinal()]) {
/*     */         case 1:
/*  84 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case 2:
/*  86 */           elementType.skip(input, accounter);
/*     */           break;
/*     */         case 3:
/*  89 */           elementType.skip(input, accounter);
/*     */           break;
/*     */         
/*     */         default:
/*  93 */           switch (ListTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$ValueResult[elementType.parse(input, output, accounter).ordinal()]) {
/*     */             case 1:
/*  95 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case 2:
/*     */               break;
/*     */           }  break;
/*     */       } 
/* 100 */     }  int amountToSkip = count - 1 - i;
/* 101 */     if (amountToSkip > 0) {
/* 102 */       elementType.skip(input, amountToSkip, accounter);
/*     */     }
/* 104 */     return output.visitContainerEnd();
/*     */   }
/*     */   
/*     */   private static int readListCount(DataInput input) throws IOException {
/* 108 */     int count = input.readInt();
/* 109 */     if (count < 0) {
/* 110 */       throw new NbtFormatException("ListTag length cannot be negative: " + count);
/*     */     }
/* 112 */     return count;
/*     */   }
/*     */ 
/*     */   
/*     */   public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 117 */     accounter.pushDepth();
/*     */     try {
/* 119 */       TagType<?> type = TagTypes.getType(input.readByte());
/* 120 */       int count = input.readInt();
/* 121 */       type.skip(input, count, accounter);
/*     */     } finally {
/* 123 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public String getName() { return "LIST"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public String getPrettyName() { return "TAG_List"; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\ListTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */