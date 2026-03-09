/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UTFDataFormatException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.util.DelegateDataOutput;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NbtIo
/*     */ {
/*  31 */   private static final OpenOption[] SYNC_OUTPUT_OPTIONS = { StandardOpenOption.SYNC, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING };
/*     */ 
/*     */   
/*     */   public static CompoundTag readCompressed(Path file, NbtAccounter accounter) throws IOException {
/*  35 */     InputStream rawInput = Files.newInputStream(file, new OpenOption[0]); 
/*  36 */     try { FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(rawInput);
/*     */       
/*  38 */       try { CompoundTag compoundTag = readCompressed(fastBufferedInputStream, accounter);
/*  39 */         fastBufferedInputStream.close(); if (rawInput != null) rawInput.close();  return compoundTag; } catch (Throwable throwable) { try { fastBufferedInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (rawInput != null)
/*     */         try { rawInput.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  43 */      } private static DataInputStream createDecompressorStream(InputStream in) throws IOException { return new DataInputStream(new FastBufferedInputStream(new GZIPInputStream(in))); }
/*     */ 
/*     */ 
/*     */   
/*  47 */   private static DataOutputStream createCompressorStream(OutputStream out) throws IOException { return new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(out))); }
/*     */ 
/*     */   
/*     */   public static CompoundTag readCompressed(InputStream in, NbtAccounter accounter) throws IOException {
/*  51 */     DataInputStream dis = createDecompressorStream(in); 
/*  52 */     try { CompoundTag compoundTag = read(dis, accounter);
/*  53 */       if (dis != null) dis.close();  return compoundTag; }
/*     */     catch (Throwable throwable) { if (dis != null)
/*     */         try { dis.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  58 */      } public static void parseCompressed(Path file, StreamTagVisitor output, NbtAccounter accounter) throws IOException { InputStream rawInput = Files.newInputStream(file, new OpenOption[0]); 
/*  59 */     try { FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(rawInput);
/*     */       
/*  61 */       try { parseCompressed(fastBufferedInputStream, output, accounter);
/*  62 */         fastBufferedInputStream.close(); } catch (Throwable throwable) { try { fastBufferedInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (rawInput != null) rawInput.close();  } catch (Throwable throwable) { if (rawInput != null)
/*     */         try { rawInput.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  66 */      } public static void parseCompressed(InputStream in, StreamTagVisitor output, NbtAccounter accounter) throws IOException { DataInputStream dis = createDecompressorStream(in); 
/*  67 */     try { parse(dis, output, accounter);
/*  68 */       if (dis != null) dis.close();  } catch (Throwable throwable) { if (dis != null)
/*     */         try { dis.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  72 */      } public static void writeCompressed(CompoundTag tag, Path file) throws IOException { OutputStream out = Files.newOutputStream(file, SYNC_OUTPUT_OPTIONS); 
/*  73 */     try { OutputStream bufferedOut = new BufferedOutputStream(out); 
/*  74 */       try { writeCompressed(tag, bufferedOut);
/*  75 */         bufferedOut.close(); } catch (Throwable throwable) { try { bufferedOut.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (out != null) out.close();  } catch (Throwable throwable) { if (out != null)
/*     */         try { out.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  79 */      } public static void writeCompressed(CompoundTag tag, OutputStream out) throws IOException { DataOutputStream dos = createCompressorStream(out); 
/*  80 */     try { write(tag, dos);
/*  81 */       if (dos != null) dos.close();  }
/*     */     catch (Throwable throwable) { if (dos != null)
/*     */         try { dos.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  86 */      } public static void write(CompoundTag tag, Path file) throws IOException { OutputStream out = Files.newOutputStream(file, SYNC_OUTPUT_OPTIONS); 
/*  87 */     try { OutputStream bufferedOut = new BufferedOutputStream(out); 
/*  88 */       try { DataOutputStream dos = new DataOutputStream(bufferedOut);
/*     */         
/*  90 */         try { write(tag, dos);
/*  91 */           dos.close(); } catch (Throwable throwable) { try { dos.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  bufferedOut.close(); } catch (Throwable throwable) { try { bufferedOut.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (out != null) out.close();  } catch (Throwable throwable) { if (out != null)
/*     */         try { out.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  95 */      } public static CompoundTag read(Path file) throws IOException { if (!Files.exists(file, new java.nio.file.LinkOption[0])) {
/*  96 */       return null;
/*     */     }
/*     */     
/*  99 */     InputStream in = Files.newInputStream(file, new OpenOption[0]); 
/* 100 */     try { DataInputStream dis = new DataInputStream(in);
/*     */       
/* 102 */       try { CompoundTag compoundTag = read(dis, NbtAccounter.unlimitedHeap());
/* 103 */         dis.close(); if (in != null) in.close();  return compoundTag; } catch (Throwable throwable) { try { dis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (in != null)
/*     */         try { in.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 107 */      } public static CompoundTag read(DataInput input) throws IOException { return read(input, NbtAccounter.unlimitedHeap()); }
/*     */ 
/*     */   
/*     */   public static CompoundTag read(DataInput input, NbtAccounter accounter) throws IOException {
/* 111 */     Tag tag = readUnnamedTag(input, accounter);
/* 112 */     if (tag instanceof CompoundTag) {
/* 113 */       return (CompoundTag)tag;
/*     */     }
/* 115 */     throw new IOException("Root tag must be a named compound tag");
/*     */   }
/*     */ 
/*     */   
/* 119 */   public static void write(CompoundTag tag, DataOutput output) throws IOException { writeUnnamedTagWithFallback(tag, output); }
/*     */ 
/*     */   
/*     */   public static void parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 123 */     TagType<?> type = TagTypes.getType(input.readByte());
/* 124 */     if (type == EndTag.TYPE) {
/* 125 */       if (output.visitRootEntry(EndTag.TYPE) == StreamTagVisitor.ValueResult.CONTINUE) {
/* 126 */         output.visitEnd();
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 131 */     switch (output.visitRootEntry(type)) {
/*     */ 
/*     */       
/*     */       case BREAK:
/* 135 */         StringTag.skipString(input);
/* 136 */         type.skip(input, accounter);
/*     */         break;
/*     */       case CONTINUE:
/* 139 */         StringTag.skipString(input);
/* 140 */         type.parse(input, output, accounter);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Tag readAnyTag(DataInput input, NbtAccounter accounter) throws IOException {
/* 146 */     byte type = input.readByte();
/* 147 */     if (type == 0) {
/* 148 */       return EndTag.INSTANCE;
/*     */     }
/* 150 */     return readTagSafe(input, accounter, type);
/*     */   }
/*     */   
/*     */   public static void writeAnyTag(Tag tag, DataOutput output) throws IOException {
/* 154 */     output.writeByte(tag.getId());
/* 155 */     if (tag.getId() == 0) {
/*     */       return;
/*     */     }
/* 158 */     tag.write(output);
/*     */   }
/*     */   
/*     */   public static void writeUnnamedTag(Tag tag, DataOutput output) throws IOException {
/* 162 */     output.writeByte(tag.getId());
/* 163 */     if (tag.getId() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 168 */     output.writeUTF("");
/*     */     
/* 170 */     tag.write(output);
/*     */   }
/*     */ 
/*     */   
/* 174 */   public static void writeUnnamedTagWithFallback(Tag tag, DataOutput output) throws IOException { writeUnnamedTag(tag, new StringFallbackDataOutput(output)); }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public static Tag readUnnamedTag(DataInput input, NbtAccounter accounter) throws IOException {
/* 179 */     byte type = input.readByte();
/* 180 */     if (type == 0) {
/* 181 */       return EndTag.INSTANCE;
/*     */     }
/*     */ 
/*     */     
/* 185 */     StringTag.skipString(input);
/*     */     
/* 187 */     return readTagSafe(input, accounter, type);
/*     */   }
/*     */   
/*     */   private static Tag readTagSafe(DataInput input, NbtAccounter accounter, byte type) {
/*     */     try {
/* 192 */       return TagTypes.getType(type).load(input, accounter);
/* 193 */     } catch (IOException e) {
/* 194 */       CrashReport report = CrashReport.forThrowable(e, "Loading NBT data");
/* 195 */       CrashReportCategory category = report.addCategory("NBT Tag");
/* 196 */       category.setDetail("Tag type", Byte.valueOf(type));
/* 197 */       throw new ReportedNbtException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class StringFallbackDataOutput
/*     */     extends DelegateDataOutput {
/* 203 */     public StringFallbackDataOutput(DataOutput parent) { super(parent); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void writeUTF(String s) throws IOException {
/*     */       try {
/* 209 */         super.writeUTF(s);
/* 210 */       } catch (UTFDataFormatException exception) {
/* 211 */         Util.logAndPauseIfInIde("Failed to write NBT String", exception);
/*     */         
/* 213 */         super.writeUTF("");
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\NbtIo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */