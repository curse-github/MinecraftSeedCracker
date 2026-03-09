/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
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
/*    */ class null
/*    */   extends Object
/*    */   implements Codec<A>
/*    */ {
/*    */   public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 59 */     return codec.encode(input, ops, prefix).flatMap(data -> ops.mergeToMap(data, ops
/* 60 */           .createString("DataVersion"), ops.createInt(DataFixTypes.currentVersion())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 67 */     Objects.requireNonNull(ops);
/*    */ 
/*    */     
/* 70 */     int fromVersion = ((Integer)ops.get(input, "DataVersion").flatMap(ops::getNumberValue).map(Number::intValue).result().orElse(Integer.valueOf(defaultVersion))).intValue();
/* 71 */     Dynamic<T> dataWithoutVersion = new Dynamic<T>(ops, ops.remove(input, "DataVersion"));
/* 72 */     Dynamic<T> fixedData = DataFixTypes.this.updateToCurrentVersion(dataFixer, dataWithoutVersion, fromVersion);
/* 73 */     return codec.decode(fixedData);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\DataFixTypes$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */