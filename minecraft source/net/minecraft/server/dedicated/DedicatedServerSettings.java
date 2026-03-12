/*    */ package net.minecraft.server.dedicated;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class DedicatedServerSettings {
/*    */   private final Path source;
/*    */   private DedicatedServerProperties properties;
/*    */   
/*    */   public DedicatedServerSettings(Path source) {
/* 11 */     this.source = source;
/* 12 */     this.properties = DedicatedServerProperties.fromFile(source);
/*    */   }
/*    */ 
/*    */   
/* 16 */   public DedicatedServerProperties getProperties() { return this.properties; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public void forceSave() { this.properties.store(this.source); }
/*    */ 
/*    */   
/*    */   public DedicatedServerSettings update(UnaryOperator<DedicatedServerProperties> mutator) {
/* 24 */     (this.properties = (DedicatedServerProperties)mutator.apply(this.properties)).store(this.source);
/* 25 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dedicated\DedicatedServerSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */