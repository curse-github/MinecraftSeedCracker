/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class PoiTypeRenameFix extends AbstractPoiSectionFix {
/*    */   private final Function<String, String> renamer;
/*    */   
/*    */   public PoiTypeRenameFix(Schema outputSchema, String name, Function<String, String> renamer) {
/* 14 */     super(outputSchema, name);
/* 15 */     this.renamer = renamer;
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Stream<Dynamic<T>> processRecords(Stream<Dynamic<T>> stream) {
/* 20 */     return stream.map(element -> 
/* 21 */         element.update("type", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\PoiTypeRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */