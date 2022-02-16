// Derek Loza, Alejandro Maldonado, Matthew Moffitt

class TreeString
{
  private String str;

  public void Print()
  {
    System.out.print(str);
  }

  public void Add(String toAdd)
  {
    str = str + toAdd;
  }

  public void Add(int depth, String toAdd)
  {
    for (int i = 0; i < depth; i++)
    {
      Add("  ");
    }
    Add(toAdd);
  }

  public TreeString()
  {
    str = "";
  }
}