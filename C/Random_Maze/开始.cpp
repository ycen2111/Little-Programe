#include <iostream>
#include <cstdlib>
#include <fstream>
using namespace std;

void output(int,int,char[55][55]);

int main()
{
    for (;;)
    {
        int row_number,column_number;
        char maze[55][55];
        ifstream DataFile;
        ifstream DataFileSolve;

        cout<<"START: "<<endl;
        system("pause");

        system("start 随机迷宫生成.exe");
        DataFile.open("maze.txt",ios::in);
        if (DataFile)
        {
            DataFile>>row_number>>column_number;
            for (int i=0;i<row_number;i++)
            {
                for (int j=0;j<column_number;j++)
                    DataFile>>maze[i][j];
            }
        }
        DataFile.close();
        output(row_number,column_number,maze);

        cout<<"显示最简答案";
        system("pause");

        system("start 提供迷宫最短路线.exe");
        DataFileSolve.open("maze_solve.txt",ios::in);
        if (DataFileSolve)
        {
            DataFileSolve>>row_number>>column_number;
            for (int i=0;i<row_number;i++)
            {
                for (int j=0;j<column_number;j++)
                    DataFileSolve>>maze[i][j];
            }
        }
        DataFileSolve.close();
        output(row_number,column_number,maze);

        cout<<"Enter y to continue: ";
        char choice;
        cin>>choice;
        if (choice!='y')
            break;
    }

    return 0;
}

void output(int row,int column,char matrix[55][55])
{
    for (int i=0;i<row;i++)
    {
        for (int j=0;j<column;j++)
            cout<<matrix[i][j]<<" ";
        cout<<endl;
    }
}
