/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class StructureProcessorList
/*    */ {
/*    */   private final List<StructureProcessor> list;
/*    */   
/*  9 */   public StructureProcessorList(List<StructureProcessor> list) { this.list = list; }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public List<StructureProcessor> list() { return this.list; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public String toString() { return "ProcessorList[" + String.valueOf(this.list) + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessorList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */