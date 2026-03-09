/*    */ package net.minecraft.data.structures;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.DetectedVersion;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.server.Bootstrap;
/*    */ 
/*    */ public class SnbtDatafixer
/*    */ {
/*    */   public static void main(String[] args) throws IOException {
/* 19 */     SharedConstants.setVersion(DetectedVersion.BUILT_IN);
/* 20 */     Bootstrap.bootStrap();
/* 21 */     for (String dir : args) {
/* 22 */       updateInDirectory(dir);
/*    */     }
/*    */   }
/*    */   
/*    */   private static void updateInDirectory(String structureDir) throws IOException {
/* 27 */     Stream<Path> walk = Files.walk(Paths.get(structureDir, new String[0]), new java.nio.file.FileVisitOption[0]); try {
/* 28 */       walk.filter(path -> path.toString().endsWith(".snbt")).forEach(path -> {
/*    */             try {
/* 30 */               String snbt = Files.readString(path);
/* 31 */               CompoundTag readSnbt = NbtUtils.snbtToStructure(snbt);
/* 32 */               CompoundTag updatedTag = StructureUpdater.update(path.toString(), readSnbt);
/* 33 */               NbtToSnbt.writeSnbt(CachedOutput.NO_CACHE, path, NbtUtils.structureToSnbt(updatedTag));
/* 34 */             } catch (CommandSyntaxException|IOException e) {
/* 35 */               throw new RuntimeException(e);
/*    */             } 
/*    */           });
/* 38 */       if (walk != null) walk.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (walk != null)
/*    */         try {
/*    */           walk.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\structures\SnbtDatafixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */