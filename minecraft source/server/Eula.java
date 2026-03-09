/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Properties;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class Eula
/*    */ {
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Path file;
/*    */   private final boolean agreed;
/*    */   
/*    */   public Eula(Path file) {
/* 21 */     this.file = file;
/* 22 */     this.agreed = (SharedConstants.IS_RUNNING_IN_IDE || readFile());
/*    */   }
/*    */   private boolean readFile() {
/*    */     
/* 26 */     try { InputStream input = Files.newInputStream(this.file, new java.nio.file.OpenOption[0]); 
/* 27 */       try { Properties properties = new Properties();
/* 28 */         properties.load(input);
/* 29 */         boolean bool = Boolean.parseBoolean(properties.getProperty("eula", "false"));
/* 30 */         if (input != null) input.close();  return bool; } catch (Throwable throwable) { if (input != null) try { input.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception ignored)
/* 31 */     { LOGGER.warn("Failed to load {}", this.file);
/* 32 */       saveDefaults();
/*    */       
/* 34 */       return false; }
/*    */   
/*    */   }
/*    */   
/* 38 */   public boolean hasAgreedToEULA() { return this.agreed; }
/*    */ 
/*    */   
/*    */   private void saveDefaults() {
/* 42 */     if (SharedConstants.IS_RUNNING_IN_IDE)
/*    */       return; 
/*    */     
/* 45 */     try { OutputStream output = Files.newOutputStream(this.file, new java.nio.file.OpenOption[0]); 
/* 46 */       try { Properties properties = new Properties();
/* 47 */         properties.setProperty("eula", "false");
/* 48 */         properties.store(output, "By changing the setting below to TRUE you are indicating your agreement to our EULA (" + String.valueOf(CommonLinks.EULA) + ").");
/* 49 */         if (output != null) output.close();  } catch (Throwable throwable) { if (output != null) try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 50 */     { LOGGER.warn("Failed to save {}", this.file, e); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\Eula.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */