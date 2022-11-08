//read the maze from maze.txt, and find the correct way
#include <iostream>
#include <fstream>
#include <cstdlib>
#include <ctime>
using namespace std;

const char pass_step='X';
char maze[55][55]={0};
int ways[2500][2]={0};
int extra_ways[200][2]={0};

void output(int,int);//output matrix(maze)
int find_random_way(int,int);
void search_empty_point(int,int,bool*,bool*,bool*,bool*);
void move_point(int*,int*,bool,bool,bool,bool);
void get_back(int*,int*);
void rewrite_way(int,int,int[2500][2]);
void maze_cleaner(int,int);

int main()
{
    int row_number,column_number;
    int try_number=0;
    int step,step_saved=2500;
    int ways_saved[2500][2];
    ifstream DataFile;

    srand((int)time(0));

    //cout<<"Hello"<<endl;
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

    //cout<<row_number<<" "<<column_number<<endl;
    //output(row_number,column_number);

    do
    {
        maze_cleaner(row_number,column_number);
        //output(row_number,column_number);
        step=find_random_way(row_number,column_number);
        //cout<<step<<endl;
        if (step<step_saved)
        {
            //cout<<"#Change";
            for (int i=0;i<step;i++)
            {
                ways_saved[i][0]=ways[i][0];
                ways_saved[i][1]=ways[i][1];
            }
            for (int i=step;ways_saved[i][0]!=0;i++)
            {
                ways_saved[i][0]=0;
                ways_saved[i][1]=0;
            }
            step_saved=step;
            //rewrite_way(row_number,column_number,ways_saved);
            //output(row_number,column_number);
        }
        try_number++;

        //rewrite_way(row_number,column_number,ways_saved);
        //output(row_number,column_number);
    } while (try_number<500);

    rewrite_way(row_number,column_number,ways_saved);
    maze[row_number-2][column_number-2]='X';
    output(row_number,column_number);
    //cout<<"Step: "<<step_saved<<endl;

    ofstream DataFileSolve;
    DataFileSolve.open("maze_solve.txt",ios::out);

    DataFileSolve<<row_number<<" "<<column_number<<endl;

    for (int i=0;i<row_number;i++)
    {
        for (int j=0;j<column_number;j++)
        {
            //if (maze[i][j]==0)
            //    DataFile<<"  ";
            //else
                DataFileSolve<<maze[i][j]<<" ";
        }
        DataFileSolve<<endl;
    }

    DataFile.close();

    //system("pause");

    return 0;
}

//output matrix
void output(int row,int column)
{
    for (int i=0;i<row;i++)
    {
        for (int j=0;j<column;j++)
            cout<<maze[i][j]<<" ";
        cout<<endl;
    }
}

int find_random_way(int row_number,int column_number)
{
    int step_number=-1,extra_way_number=0;//step_number==-1 just for the first "step_number++"=0
    int *s=&step_number,*e=&extra_way_number;
    int row=1, column=1;
    int *ro=&row, *c=&column;
    bool up=0,down=0,left=0,right=0;
    bool *u=&up, *d=&down, *l=&left, *r=&right;

    while (row!=row_number-2||column!=column_number-2)
    {
        step_number++;
        ways[step_number][0]=row;
        ways[step_number][1]=column;
        //cout<<row<<" "<<column<<" ";
        maze[row][column]=pass_step;
        BACK_ALREADY:
        up=0;down=0;left=0;right=0;
        search_empty_point(row,column,u,d,l,r);
        //cout<<up<<down<<left<<right<<" "<<endl;

        //cout<<(int)up+(int)down+(int)left+(int)right;
        if ((int)up+(int)down+(int)left+(int)right==1)
        {
            //cout<<"Step=1"<<endl;
            move_point(ro,c,up,down,left,right);
            //cout<<"step "<<step_number<<endl;
        }
        else if ((int)up+(int)down+(int)left+(int)right>1)
        {
            extra_way_number++;
            extra_ways[extra_way_number][0]=row;
            extra_ways[extra_way_number][1]=column;
            //cout<<"#extra way point get "<<extra_way_number<<endl;
            move_point(ro,c,up,down,left,right);
        }
        else{
            maze[row][column]='?';
            //cout<<"#dead end "<<row<<" "<<column<<endl;
            //cout<<"step: "<<step_number<<" extra_step: "<<extra_way_number<<endl;
            get_back(s,e);
            row=ways[*s][0];
            column=ways[*s][1];
            maze[row][column]='Y';
            //cout<<"#back to "<<row<<" "<<column<<endl;

            //for (int i=0;i<=step_number;i++)
             //   cout<<ways[i][0]<<" "<<ways[i][1]<<endl;

            goto BACK_ALREADY;
        }
        //output(row_number,column_number);
    }

    //output(row_number,column_number);
    return step_number;
}

void search_empty_point(int row,int column,bool *u,bool *d,bool *l,bool *r)
{
    if (maze[row-1][column]==0)
        *u=1;
    if (maze[row+1][column]==0)
        *d=1;
    if (maze[row][column-1]==0)
        *l=1;
    if (maze[row][column+1]==0)
        *r=1;
}

void move_point(int *r,int *c,bool up,bool down,bool left,bool right)
{
    int direction;
    for(;;)
    {
        direction=(rand()%4);
        //cout<<direction<<endl;
        if (direction==0&&up==1) {(*r)--; break;}
        if (direction==1&&down==1) {(*r)++; break;}
        if (direction==2&&left==1) {(*c)--; break;}
        if (direction==3&&right==1) {(*c)++; break;}
    }
}

void get_back(int *s,int *e)
{
    while (ways[*s][0]!=extra_ways[*e][0]||ways[*s][1]!=extra_ways[*e][1])
    {
        ways[*s][0]=0;
        ways[*s][1]=0;
        (*s)--;
        //cout<<"step: "<<*s<<" extra_step: "<<*e<<endl;
    }

    extra_ways[*e][0]=0;
    extra_ways[*e][1]=0;
    (*e)--;
    //cout<<"step: "<<*s<<" extra_step: "<<*e<<endl;
}

void rewrite_way(int row,int column,int ways[2500][2])
{
    maze_cleaner(row,column);

    for (int i=0;ways[i][0]!=0;i++)
        maze[ways[i][0]][ways[i][1]]='X';
}

void maze_cleaner(int row, int column)
{
    for (int i=0;i<row;i++)
    {
        for (int j=0;j<column;j++)
            if (maze[i][j]=='X'||maze[i][j]=='Y'||maze[i][j]=='?')
                maze[i][j]=0;
    }
}
