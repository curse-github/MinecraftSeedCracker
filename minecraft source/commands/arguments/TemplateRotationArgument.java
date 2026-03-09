/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ public class TemplateRotationArgument
/*    */   extends StringRepresentableArgument<Rotation> {
/*  9 */   private TemplateRotationArgument() { super(Rotation.CODEC, Rotation::values); }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static TemplateRotationArgument templateRotation() { return new TemplateRotationArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static Rotation getRotation(CommandContext<CommandSourceStack> context, String name) { return (Rotation)context.getArgument(name, Rotation.class); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\TemplateRotationArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */