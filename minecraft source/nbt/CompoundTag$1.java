/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.DataInput;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
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
/*     */ class null
/*     */   extends Object
/*     */   implements TagType.VariableSize<CompoundTag>
/*     */ {
/*     */   public CompoundTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  67 */     accounter.pushDepth();
/*     */     try {
/*  69 */       return loadCompound(input, accounter);
/*     */     } finally {
/*  71 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static CompoundTag loadCompound(DataInput input, NbtAccounter accounter) throws IOException {
/*  76 */     accounter.accountBytes(48L);
/*     */     
/*  78 */     Map<String, Tag> values = Maps.newHashMap();
/*     */     byte tagType;
/*  80 */     while ((tagType = input.readByte()) != 0) {
/*  81 */       String key = readString(input, accounter);
/*  82 */       Tag tag = CompoundTag.readNamedTagData(TagTypes.getType(tagType), key, input, accounter);
/*  83 */       if (values.put(key, tag) == null) {
/*  84 */         accounter.accountBytes(36L);
/*     */       }
/*     */     } 
/*  87 */     return new CompoundTag(values);
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  92 */     accounter.pushDepth();
/*     */     try {
/*  94 */       return parseCompound(input, output, accounter);
/*     */     } finally {
/*  96 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static StreamTagVisitor.ValueResult parseCompound(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 101 */     accounter.accountBytes(48L);
/*     */ 
/*     */     
/*     */     byte tagTypeId;
/*     */     
/* 106 */     while ((tagTypeId = input.readByte()) != 0) {
/* 107 */       TagType<?> tagType = TagTypes.getType(tagTypeId);
/*     */       
/* 109 */       switch (CompoundTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$EntryResult[output.visitEntry(tagType).ordinal()]) {
/*     */         case 1:
/* 111 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case 2:
/* 113 */           StringTag.skipString(input);
/* 114 */           tagType.skip(input, accounter);
/*     */           break;
/*     */         case 3:
/* 117 */           StringTag.skipString(input);
/* 118 */           tagType.skip(input, accounter);
/*     */           continue;
/*     */       } 
/*     */       
/* 122 */       String key = readString(input, accounter);
/* 123 */       switch (CompoundTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$EntryResult[output.visitEntry(tagType, key).ordinal()]) {
/*     */         case 1:
/* 125 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case 2:
/* 127 */           tagType.skip(input, accounter);
/*     */           break;
/*     */         case 3:
/* 130 */           tagType.skip(input, accounter);
/*     */           continue;
/*     */       } 
/*     */       
/* 134 */       accounter.accountBytes(36L);
/* 135 */       switch (CompoundTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$ValueResult[tagType.parse(input, output, accounter).ordinal()]) {
/*     */         case 1:
/* 137 */           return StreamTagVisitor.ValueResult.HALT;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 143 */     if (tagTypeId != 0) {
/* 144 */       while ((tagTypeId = input.readByte()) != 0) {
/* 145 */         StringTag.skipString(input);
/* 146 */         TagTypes.getType(tagTypeId).skip(input, accounter);
/*     */       } 
/*     */     }
/*     */     
/* 150 */     return output.visitContainerEnd();
/*     */   }
/*     */   
/*     */   private static String readString(DataInput input, NbtAccounter accounter) throws IOException {
/* 154 */     String key = input.readUTF();
/* 155 */     accounter.accountBytes(28L);
/* 156 */     accounter.accountBytes(2L, key.length());
/* 157 */     return key;
/*     */   }
/*     */ 
/*     */   
/*     */   public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 162 */     accounter.pushDepth();
/*     */     try {
/*     */       byte tagTypeId;
/* 165 */       while ((tagTypeId = input.readByte()) != 0) {
/* 166 */         StringTag.skipString(input);
/* 167 */         TagTypes.getType(tagTypeId).skip(input, accounter);
/*     */       } 
/*     */     } finally {
/* 170 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public String getName() { return "COMPOUND"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 181 */   public String getPrettyName() { return "TAG_Compound"; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\CompoundTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */