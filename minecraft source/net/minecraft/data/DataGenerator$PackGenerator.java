/*    */ package net.minecraft.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PackGenerator
/*    */ {
/*    */   private final boolean toRun;
/*    */   private final String providerPrefix;
/*    */   private final PackOutput output;
/*    */   
/*    */   private PackGenerator(boolean toRun, String providerPrefix, PackOutput output) {
/* 79 */     this.toRun = toRun;
/* 80 */     this.providerPrefix = providerPrefix;
/* 81 */     this.output = output;
/*    */   }
/*    */   
/*    */   public <T extends DataProvider> T addProvider(DataProvider.Factory<T> factory) {
/* 85 */     T provider = (T)factory.create(this.output);
/* 86 */     String providerId = this.providerPrefix + "/" + this.providerPrefix;
/* 87 */     if (!DataGenerator.this.allProviderIds.add(providerId)) {
/* 88 */       throw new IllegalStateException("Duplicate provider: " + providerId);
/*    */     }
/* 90 */     if (this.toRun) {
/* 91 */       DataGenerator.this.providersToRun.put(providerId, provider);
/*    */     }
/* 93 */     return provider;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\DataGenerator$PackGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */