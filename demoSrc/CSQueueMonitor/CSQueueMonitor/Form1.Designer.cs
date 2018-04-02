namespace CSTester
{
    partial class Form1
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要修改
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.textBox_judge = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.textBox_decompose = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            this.textBox_decomposeVote = new System.Windows.Forms.TextBox();
            this.label5 = new System.Windows.Forms.Label();
            this.textBox_solve = new System.Windows.Forms.TextBox();
            this.label6 = new System.Windows.Forms.Label();
            this.textBox_solveVote = new System.Windows.Forms.TextBox();
            this.button_refresh = new System.Windows.Forms.Button();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.label7 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // textBox_judge
            // 
            this.textBox_judge.Location = new System.Drawing.Point(14, 24);
            this.textBox_judge.Multiline = true;
            this.textBox_judge.Name = "textBox_judge";
            this.textBox_judge.Size = new System.Drawing.Size(162, 322);
            this.textBox_judge.TabIndex = 0;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(35, 12);
            this.label1.TabIndex = 1;
            this.label1.Text = "judge";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(180, 9);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(59, 12);
            this.label3.TabIndex = 5;
            this.label3.Text = "decompose";
            // 
            // textBox_decompose
            // 
            this.textBox_decompose.Location = new System.Drawing.Point(182, 24);
            this.textBox_decompose.Multiline = true;
            this.textBox_decompose.Name = "textBox_decompose";
            this.textBox_decompose.Size = new System.Drawing.Size(162, 322);
            this.textBox_decompose.TabIndex = 4;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(348, 9);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(83, 12);
            this.label4.TabIndex = 7;
            this.label4.Text = "decomposeVote";
            // 
            // textBox_decomposeVote
            // 
            this.textBox_decomposeVote.Location = new System.Drawing.Point(350, 24);
            this.textBox_decomposeVote.Multiline = true;
            this.textBox_decomposeVote.Name = "textBox_decomposeVote";
            this.textBox_decomposeVote.Size = new System.Drawing.Size(162, 322);
            this.textBox_decomposeVote.TabIndex = 6;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(516, 9);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(35, 12);
            this.label5.TabIndex = 9;
            this.label5.Text = "solve";
            // 
            // textBox_solve
            // 
            this.textBox_solve.Location = new System.Drawing.Point(518, 24);
            this.textBox_solve.Multiline = true;
            this.textBox_solve.Name = "textBox_solve";
            this.textBox_solve.Size = new System.Drawing.Size(162, 322);
            this.textBox_solve.TabIndex = 8;
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(684, 9);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(59, 12);
            this.label6.TabIndex = 11;
            this.label6.Text = "solveVote";
            // 
            // textBox_solveVote
            // 
            this.textBox_solveVote.Location = new System.Drawing.Point(686, 24);
            this.textBox_solveVote.Multiline = true;
            this.textBox_solveVote.Name = "textBox_solveVote";
            this.textBox_solveVote.Size = new System.Drawing.Size(162, 322);
            this.textBox_solveVote.TabIndex = 10;
            // 
            // button_refresh
            // 
            this.button_refresh.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.button_refresh.Location = new System.Drawing.Point(350, 352);
            this.button_refresh.Name = "button_refresh";
            this.button_refresh.Size = new System.Drawing.Size(498, 40);
            this.button_refresh.TabIndex = 13;
            this.button_refresh.Text = "Refresh";
            this.button_refresh.UseVisualStyleBackColor = true;
            this.button_refresh.Click += new System.EventHandler(this.button_refresh_Click);
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(14, 371);
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(330, 21);
            this.textBox1.TabIndex = 14;
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(12, 356);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(29, 12);
            this.label7.TabIndex = 15;
            this.label7.Text = "RTID";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(856, 395);
            this.Controls.Add(this.label7);
            this.Controls.Add(this.textBox1);
            this.Controls.Add(this.button_refresh);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.textBox_solveVote);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.textBox_solve);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.textBox_decomposeVote);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.textBox_decompose);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.textBox_judge);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Workqueue Monitor of Crowd Sourcing";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox textBox_judge;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox textBox_decompose;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.TextBox textBox_decomposeVote;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.TextBox textBox_solve;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.TextBox textBox_solveVote;
        private System.Windows.Forms.Button button_refresh;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.Label label7;
    }
}

