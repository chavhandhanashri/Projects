//run as g++ clipboard -o myexe -lgdi32
#define UNICODE
#include<Windows.h>
int size;
HWND notepad;
WCHAR * ctow(char *);
LRESULT CALLBACK WndProc(HWND,UINT,WPARAM,LPARAM);
int WINAPI WinMain(HINSTANCE hInstance,HINSTANCE hPrevInstance,LPSTR lpszCmdLine,int nCmdShow)
{
    WNDCLASSEX wnd;
    TCHAR appName[] = {L"ClipBoard"};
    HWND hwnd;
    MSG msg;
    wnd.cbSize = sizeof(WNDCLASSEX);
    wnd.cbClsExtra = 0;
    wnd.cbWndExtra = 0;
    wnd.hbrBackground = (HBRUSH) GetStockObject(WHITE_BRUSH);
    wnd.hInstance = hInstance;
    wnd.lpfnWndProc = WndProc;
    wnd.lpszClassName = appName;
    wnd.lpszMenuName = NULL;
    wnd.style = CS_HREDRAW | CS_VREDRAW;
    wnd.hCursor = LoadCursor(hInstance,L"IDI_CURSOR");
    wnd.hIcon = LoadIcon(hInstance,L"IDI_ICON");
    wnd.hIconSm = LoadIcon(hInstance,L"IDI_ICON");
    RegisterClassEx(&wnd);
    hwnd=CreateWindow(appName,L"ClipBoardViewer",WS_OVERLAPPEDWINDOW,CW_USEDEFAULT,CW_USEDEFAULT,CW_USEDEFAULT,CW_USEDEFAULT,NULL,NULL,hInstance,NULL);
    if(hwnd==NULL)
    {
        MessageBox(hwnd,L"Create Window Failed",L"Error",MB_OK | MB_ICONERROR);
        return 0;
    }
    ShowWindow(hwnd,nCmdShow);
    UpdateWindow(hwnd);
    SetClipboardViewer(hwnd);
    while(GetMessage(&msg,hwnd,0,0))
        {
            TranslateMessage(&msg);
            DispatchMessage(&msg);
        }
    return msg.wParam;
}
LRESULT CALLBACK WndProc(HWND hwnd,UINT iMsg,WPARAM wParam,LPARAM lParam)
{
    switch(iMsg)
    {
        case WM_CREATE:
            ShellExecute(NULL,L"open",L"notepad.exe",NULL,0,SW_SHOW);
            Sleep(100);
            break;

        case WM_DESTROY:
            PostQuitMessage(0);
            break;

        case WM_DRAWCLIPBOARD:
            char* clipBoardMem;
            OpenClipboard(hwnd);
            HANDLE hMem = (char *)GetClipboardData(CF_TEXT);
            char *mem = (char *) GlobalLock(hMem);
            clipBoardMem = (char *)malloc(GlobalSize(mem)+1);
            strcpy(clipBoardMem,mem);
            CloseClipboard();
            GlobalUnlock(hMem);
            notepad = FindWindow(TEXT("Notepad"),NULL);
            if(notepad==INVALID_HANDLE_VALUE || notepad==NULL)
            {
                PostQuitMessage(0);
                break;
            }
            notepad = FindWindowEx(notepad,NULL,TEXT("edit"),NULL);
            //EnableWindow(notepad,false);
            SendMessage(notepad,WM_SETTEXT,0,
            (LPARAM)ctow(clipBoardMem));
            //SendMessage(notepad,WM_SETTEXT,0,(LPARAM)(clipBoardMem));
            break;
    }
    return DefWindowProc(hwnd,iMsg,wParam,lParam);
}
// Function used to convert char to wchar_t
WCHAR * ctow(char *str)
{
    const size_t cSize = strlen(str)+1;
    wchar_t* wc = new wchar_t[cSize];
    mbstowcs (wc, str, cSize);
    size = cSize;
    return wc;
}